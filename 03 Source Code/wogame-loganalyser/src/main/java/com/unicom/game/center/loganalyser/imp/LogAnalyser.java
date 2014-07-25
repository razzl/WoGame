package com.unicom.game.center.loganalyser.imp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.unicom.game.center.business.AdTrafficBusiness;
import com.unicom.game.center.business.KeywordBusiness;
import com.unicom.game.center.business.LoginInfoBusiness;
import com.unicom.game.center.business.PageTrafficBusiness;
import com.unicom.game.center.business.ProductBusiness;
import com.unicom.game.center.db.domain.KeywordDomain;
import com.unicom.game.center.log.model.GameTraffic;
import com.unicom.game.center.log.model.KeyWord;
import com.unicom.game.center.log.model.PageTraffic;
import com.unicom.game.center.log.model.Product;
import com.unicom.game.center.log.model.UserCount;
import com.unicom.game.center.loganalyser.ILogAnalyser;
import com.unicom.game.center.utils.DateUtils;
import com.unicom.game.center.utils.FileUtils;
import com.unicom.game.center.utils.Logging;

@Component
public class LogAnalyser implements ILogAnalyser {
    @Autowired
    private LoginInfoBusiness userCountBusiness;
    @Autowired
    private PageTrafficBusiness pageTrafficBusiness;
    @Autowired
    private AdTrafficBusiness gameTrafficBusiness;
    @Autowired
    private KeywordBusiness keywordBusiness;
    @Autowired
    private ProductBusiness productBusiness;

    @Value("#{properties['log.file.path']}")
    private String logFilePath;

    @Value("#{properties['latest.log.fileInfoNumber']}")
    private String logInfoNumberFile;

    @Value("#{properties['latest.log.fileInfo']}")
    private String logInfoFile;

    @Value("#{properties['log.bak.path']}")
    private String logBakPath;

    @Override
    public void doDownloadCountDomainsSave() throws Exception{

    }

    @Override
    public void doReportDomainsSave() throws Exception{

    }
    
    @Override
    public void doLogAnalyse(){
        Logging.logDebug("----- doLogAnaylyse start -----");
        try{
            doLogAnalyse1();
            doLogAnalyse2();
        }catch(Exception e){
            Logging.logError("Error occurs in doLogAnaylyse ", e);
        }
        Logging.logDebug("----- doLogAnaylyse end -----");
    }

    @Override
    public void doPackageInfoDomainsSave() throws Exception {
    	
    }    

    private void  keyWordDispose(String value, Map<String,KeyWord> keyMapSave, Map<String,KeyWord> keyMapUpdate){
        KeyWord keyWord = null;
        Date today = new Date();
        Date yesterday = DateUtils.getDayByInterval(today,-1);
        if(!value.substring(0,3).trim().equals("")){
            int channelId = Integer.parseInt(value.substring(0,3).trim());
            if(channelId != 0){
                String keywordValue = value.substring(3,value.length());
                KeywordDomain keywordDomain = keywordBusiness.getKeyWord(keywordValue,channelId);
                if (keywordDomain == null) {
                    if(keyMapSave.containsKey(value)){
                        keyWord = keyMapSave.get(value) ;
                        keyWord.setCount(keyWord.getCount() + 1);
                    }else{
                        keyWord = new KeyWord();
                        keyWord.setCount(1);
                    }
                    keyWord.setKeyword(keywordValue);
                    keyWord.setChannelId(channelId);
                    keyWord.setDateCreated(yesterday);
                    keyWord.setDateModified(today);
                    keyMapSave.put(value, keyWord);
                }else{
                    if(!keyMapUpdate.containsKey(value)){
                        keyWord = new KeyWord();
                        keyWord.setId(keywordDomain.getId());
                        keyWord.setKeyword(keywordValue);
                        keyWord.setChannelId(channelId);
                        keyWord.setCount(keywordDomain.getCount());
                        keyMapUpdate.put(value,keyWord);
                        keyWord = keyMapUpdate.get(value);
                    } else {
                        keyWord = keyMapUpdate.get(value) ;
                        keyWord.setId(keyWord.getId());
                    }
                    keyWord.setKeyword(keywordValue);
                    keyWord.setChannelId(channelId);
                    keyWord.setCount(keyWord.getCount() + 1);
                    keyWord.setDateCreated(yesterday);
                    keyWord.setDateModified(today);
                    keyMapUpdate.put(value, keyWord);
                }
            }
        }
    }


    private static Map woGameInfoNumberReader(File file){
        String fileContent = "";
        String contentTemp = "";
        BufferedReader reader = null;
        Map<String,Integer> numberCountMap = new HashMap<String, Integer>();
        try {
            reader = new BufferedReader(new UnicodeReader(new FileInputStream(file), "UTF-8"));
            while ((contentTemp = reader.readLine()) !=  null){
                if(fileContent.equals("")){
                    fileContent = contentTemp;
                } else{
                    fileContent = fileContent + " " + contentTemp;
                }
            }
        } catch (IOException e) {
            Logging.logError("Error occurs in woGameInfoNumberReader", e);
        }
        if(fileContent.equals("")){
            return numberCountMap;
        }else{
            StringTokenizer tokenizer = new StringTokenizer(fileContent);
            while(tokenizer.hasMoreTokens()){
                String splitWord = tokenizer.nextToken();
                if(numberCountMap.containsKey(splitWord)){
                    int count = numberCountMap.get(splitWord);
                    numberCountMap.put(splitWord,count+1);
                }else{
                    numberCountMap.put(splitWord,1);
                }
            }
        }
        return numberCountMap;
    }

    private void woGameInfoNumberParse(Map<String,Integer> numberCountMap,Date fileDate){
        Map<Integer,UserCount> userCountMap = new HashMap<Integer, UserCount>();
        Map<Integer,PageTraffic> pageTrafficMap = new HashMap<Integer, PageTraffic>();
        Map<Integer,GameTraffic> gameTrafficMap = new HashMap<Integer,GameTraffic>();
        UserCount userCount = null;
        PageTraffic pageTraffic = null;
        GameTraffic gameTraffic = null;
        Integer channelId = null;
        int id = 0;
        int clickThrough = 0;
        int adType = 0;
        int adId = 0;
        Iterator iterator = numberCountMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry =(Map.Entry) iterator.next();
            String key = entry.getKey().toString();
            int val = Integer.parseInt(entry.getValue().toString());
            String firstTwoCharacters = key.substring(0,2);
            if(firstTwoCharacters.equalsIgnoreCase("80")||firstTwoCharacters.equalsIgnoreCase("81")){
                if(!key.substring(2,key.length()).trim().equals("")){
                    channelId = Integer.parseInt(key.substring(2,key.length()).trim());
                    if(channelId != 0){
                        if (userCountMap.containsKey(channelId)){
                            userCount = userCountMap.get(channelId);
                            if(userCount != null){
                                if(firstTwoCharacters.equalsIgnoreCase("80")){
                                    userCount.setNew_user_count(userCount.getNew_user_count() + val);
                                } else if(firstTwoCharacters.equalsIgnoreCase("81")){
                                    userCount.setOld_user_count(userCount.getOld_user_count() + val);
                                }
                            } else{
                                userCount = new UserCount();
                                if(firstTwoCharacters.equalsIgnoreCase("80")){
                                    userCount.setNew_user_count(val);
                                } else if(firstTwoCharacters.equalsIgnoreCase("81")){
                                    userCount.setOld_user_count(val);
                                }
                            }
                        } else {
                            userCount = new UserCount();
                            if(firstTwoCharacters.equalsIgnoreCase("80")){
                                userCount.setNew_user_count(val);
                            } else if(firstTwoCharacters.equalsIgnoreCase("81")){
                                userCount.setOld_user_count(val);
                            }
                        }
                        userCount.setChannelId(channelId);
                        userCount.setDateCreated(fileDate);
                        userCountMap.put(channelId,userCount);
                    }
                }
            } else if(firstTwoCharacters.equalsIgnoreCase("61")||firstTwoCharacters.equalsIgnoreCase("62")||firstTwoCharacters.equalsIgnoreCase("63")||firstTwoCharacters.equalsIgnoreCase("64")){
                if(!key.substring(2,key.length()).trim().equals("")){
                    channelId = Integer.parseInt(key.substring(2,key.length()).trim());
                    if(channelId != 0){
                        if (pageTrafficMap.containsKey(channelId)){
                            pageTraffic = pageTrafficMap.get(channelId);
                            if(pageTraffic != null){
                                if (firstTwoCharacters.equalsIgnoreCase("61")){
                                    pageTraffic.setHomepage(pageTraffic.getHomepage() + val);
                                } else if (firstTwoCharacters.equalsIgnoreCase("62")){
                                    pageTraffic.setCategory(pageTraffic.getCategory() + val);
                                } else if (firstTwoCharacters.equalsIgnoreCase("63")){
                                    pageTraffic.setHotlist(pageTraffic.getHotlist() + val);
                                } else if (firstTwoCharacters.equalsIgnoreCase("64")){
                                    pageTraffic.setLatest(pageTraffic.getLatest() + val);
                                }
                            } else {
                                pageTraffic = new PageTraffic();
                                if (firstTwoCharacters.equalsIgnoreCase("61")){
                                    pageTraffic.setHomepage(val);
                                } else if (firstTwoCharacters.equalsIgnoreCase("62")){
                                    pageTraffic.setCategory(val);
                                } else if (firstTwoCharacters.equalsIgnoreCase("63")){
                                    pageTraffic.setHotlist(val);
                                } else if (firstTwoCharacters.equalsIgnoreCase("64")){
                                    pageTraffic.setLatest(val);
                                }
                            }
                        } else {
                            pageTraffic = new PageTraffic();
                            if (firstTwoCharacters.equalsIgnoreCase("61")){
                                pageTraffic.setHomepage(val);
                            } else if (firstTwoCharacters.equalsIgnoreCase("62")){
                                pageTraffic.setCategory(val);
                            } else if (firstTwoCharacters.equalsIgnoreCase("63")){
                                pageTraffic.setHotlist(val);
                            } else if (firstTwoCharacters.equalsIgnoreCase("64")){
                                pageTraffic.setLatest(val);
                            }
                        }
                        pageTraffic.setChannelId(channelId);
                        pageTraffic.setDateCreated(fileDate);
                        pageTrafficMap.put(channelId,pageTraffic);
                    }
                }
            }else if(firstTwoCharacters.equalsIgnoreCase("50")||firstTwoCharacters.equalsIgnoreCase("51")){
                if (firstTwoCharacters.equalsIgnoreCase("50")){
                    if(!key.substring(8,key.length()).trim().equals("")){
                        channelId = Integer.parseInt(key.substring(8,key.length()).trim().replaceAll("^(0+)", ""));
                        if(channelId != 0){
                            gameTraffic = new GameTraffic();
                            gameTraffic.setSort(Integer.parseInt(key.substring(6,8)));
                            gameTraffic.setAdType(Integer.parseInt(key.substring(4,6)));
                            gameTraffic.setAdId(Integer.parseInt(key.substring(2, 4)));
                            gameTraffic.setClickThrough(clickThrough);
                            gameTraffic.setChannelId(channelId);
                            gameTraffic.setDateCreated(fileDate);
                            adId = gameTraffic.getAdId();
                            adType = gameTraffic.getAdType();
                            gameTrafficMap.put(++id, gameTraffic);
                        }
                    }
                } else if(firstTwoCharacters.equalsIgnoreCase("51")){
                    if(!key.substring(4,key.length()).trim().equals("")){
                        channelId = Integer.parseInt(key.substring(4,key.length()).trim().replaceAll("^(0+)", ""));
                        if(channelId != 0){
                            if(!gameTrafficMap.containsKey(channelId)){
                                gameTraffic = new GameTraffic();
                                gameTraffic.setClickThrough(val);
                                clickThrough = gameTraffic.getClickThrough();
                            } else{
                                gameTraffic = new GameTraffic();
                                gameTraffic.setClickThrough(gameTraffic.getClickThrough() + val);
                                clickThrough = gameTraffic.getClickThrough();
                            }
                            gameTraffic.setSort(Integer.parseInt(key.substring(2,4)));
                            gameTraffic.setAdType(adType);
                            gameTraffic.setAdId(adId);
                            gameTraffic.setChannelId(channelId);
                            gameTraffic.setDateCreated(fileDate);
                            gameTrafficMap.put(++id,gameTraffic);
                        }
                    }
                }
            }
        }
        numberCountMap.clear();
        userCountBusiness.typeConversion(userCountMap);
        userCountMap.clear();
        pageTrafficBusiness.typeConversion(pageTrafficMap);
        pageTrafficMap.clear();
        gameTrafficBusiness.typeConversion(gameTrafficMap);
        gameTrafficMap.clear();
    }

    private void woGameInfoParse(String tempString, Date fileDate, Map<String,KeyWord> keyMapSave, Map<String,KeyWord> keyMapUpdate, Map<String,Product> productMap){
        Product product = null;
        String surplus = null;
        String firstTwoCharacters = tempString.substring(0, 2);
        if(firstTwoCharacters.equalsIgnoreCase("40")){
            surplus = tempString.substring(2,tempString.length());
            if(Integer.parseInt(surplus.substring(0,3).trim()) != 0) {
                keyWordDispose(surplus,keyMapSave,keyMapUpdate);
            }
        } else if(firstTwoCharacters.equalsIgnoreCase("30")){
            String product_id = tempString.substring(5,15).trim();
            String channel_id = tempString.substring(2,5).trim();
            String product_name = tempString.substring(15,257).trim();
            String product_icon = tempString.substring(257,tempString.length()).trim();
            surplus = tempString.substring(2,5) + product_name;
            if(Integer.parseInt(surplus.substring(0,3).trim()) != 0) {
                keyWordDispose(surplus,keyMapSave,keyMapUpdate);
            }
            boolean flag =  productBusiness.checkId(product_id);
            if(flag && Integer.parseInt(product_id) != 0){
                product = new Product();
                product.setProduct_id(product_id);
                product.setProduct_name(product_name);
                product.setProduct_icon(product_icon);
                product.setDateCreated(fileDate);
                productMap.put(product_id,product);
            }
        }
    }

    private void doLogAnalyse1(){
        Map<String,Integer> numberCountMap = null;
        File file = null;
        String dateBefore = null;
        Date today = new Date();
        Date yesterday = DateUtils.getDayByInterval(today,-1);
        String fileDate = DateUtils.formatDateToString(yesterday,"yyyy-MM-dd");
        String currentFileName = "wogamecenter_info_number."+fileDate+".log";
        try {
            List<String> currentFileList = FileUtils.readFileByRow(logInfoNumberFile);
            if(currentFileList.size()>0){
                dateBefore = currentFileList.get(0);
            }else{
                dateBefore = new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.getDayByInterval(today,-2));
            }
            String dateNow = new SimpleDateFormat("yyyy-MM-dd").format(yesterday);
            int compareDateNum = DateUtils.compareLogDate(dateBefore,dateNow);
            switch (compareDateNum){
                case -1:
                    FileUtils.writeFileOverWrite(logInfoNumberFile,dateNow);
                    file = new File(logFilePath+"/"+currentFileName);
                    if(!file.exists()){
                        file = new File(logFilePath+"/"+"wogamecenter_info_number.log");
                        if(file.exists()){
                            String loseFileDate = new SimpleDateFormat("yyyy-MM-dd").format(file.lastModified());
                            if(loseFileDate.equals(dateNow)){
                                numberCountMap = woGameInfoNumberReader(file);
                                woGameInfoNumberParse(numberCountMap, yesterday);
                            }
                        }
                    }else {
                        numberCountMap = woGameInfoNumberReader(file);
                        woGameInfoNumberParse(numberCountMap, yesterday);
                    }
                    File newPath = new File(logBakPath);
                    if(!newPath.exists()){
                        newPath.mkdirs();
                    }
                    org.apache.commons.io.FileUtils.copyFileToDirectory(file, newPath);
                    file.getAbsoluteFile().delete();
                    break;
                case 1:
                case 0:
                    break;
            }
        } catch (Exception e) {
            Logging.logError("Error occurs in doLogAnalyse1 ", e);
        }
    }

    private void doLogAnalyse2(){
        String dateBefore = null;
        String tempString = null;
        File file = null;
        Date today = new Date();
        Date yesterday = DateUtils.getDayByInterval(today, -1);
        String fileDate = DateUtils.formatDateToString(yesterday,"yyyy-MM-dd");
        Map<String,KeyWord> keyMapSave = new HashMap<String, KeyWord>();
        Map<String,KeyWord> keyMapUpdate = new HashMap<String, KeyWord>();
        Map<String,Product> productMap = new HashMap<String, Product>();
        String currentFileName = "wogamecenter_info."+fileDate+".log";
        try {

            List<String> currentFileList = FileUtils.readFileByRow(logInfoFile);
            if(currentFileList.size()>0){
                dateBefore = currentFileList.get(0);
            }else{
                dateBefore = new SimpleDateFormat("yyyy-MM-dd").format(DateUtils.getDayByInterval(today,-2));
            }
            String dateNow = new SimpleDateFormat("yyyy-MM-dd").format(yesterday);
            int compareDateNum = DateUtils.compareLogDate(dateBefore,dateNow);
            switch (compareDateNum){
                case -1:
                    FileUtils.writeFileOverWrite(logInfoFile,dateNow);
                    file = new File(logFilePath+"/"+currentFileName);
                    if(!file.exists()){
                        file = new File(logFilePath+"/"+"wogamecenter_info.log");
                        if(file.exists()){
                            String loseFileDate = new SimpleDateFormat("yyyy-MM-dd").format(file.lastModified());
                            if(loseFileDate.equals(dateNow)){
                                BufferedReader reader = new BufferedReader(new UnicodeReader(new FileInputStream(file), "UTF-8"));
                                while ((tempString = reader.readLine()) != null){
                                    woGameInfoParse(tempString,yesterday,keyMapSave,keyMapUpdate,productMap);
                                }
                            }
                        }
                    } else {
                        BufferedReader reader = new BufferedReader(new UnicodeReader(new FileInputStream(file), "UTF-8"));
                        while ((tempString = reader.readLine()) != null){
                            woGameInfoParse(tempString,yesterday,keyMapSave,keyMapUpdate,productMap);
                        }
                    }
                    break;
                case 1:
                case 0:
                    break;
            }
            keywordBusiness.typeConversionSave(keyMapSave);
            if(keyMapUpdate.size()>=1){
                keywordBusiness.typeConversionUpdate(keyMapUpdate);
            }
            productBusiness.typeConversion(productMap);

            File newPath = new File(logBakPath);
            if(!newPath.exists()){
                newPath.mkdirs();
            }
            org.apache.commons.io.FileUtils.copyFileToDirectory(file, newPath);
            file.getAbsoluteFile().delete();
        } catch (Exception e) {
            Logging.logError("Error occurs in doLogAnalyse2 ", e);
        } finally {
            keyMapSave.clear();
            keyMapUpdate.clear();
            productMap.clear();
        }
    }


}
