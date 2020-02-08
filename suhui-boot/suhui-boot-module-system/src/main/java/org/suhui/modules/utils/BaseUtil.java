package org.suhui.modules.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * Created by songyouming on 2017/7/26.
 */
public class BaseUtil {


    public static boolean Base_HasValue(Object pValue) {
        Boolean HasValue = true;
        if (pValue == (null)) {
            HasValue = false;
        }
        return HasValue;
    }

    public static boolean Base_HasValue(String pValue) {
        Boolean HasValue = true;
        if (pValue == (null)) {
            HasValue = false;
        } else if (pValue.equals("")) {
            HasValue = false;
        } else if (pValue.trim().equals("")) {
            HasValue = false;
        } else if (pValue.trim().equals("null")) {

        }
        return HasValue;
    }

    public static void Base_CheckHasValue(String Msg,Object pValue) {
        if (!Base_HasValue(pValue)) {
            throw new Error(Msg + " : 是空值");
        }
    }

    public static void Base_CheckHasValue(String Msg,String pValue) {
        if (!Base_HasValue(pValue)) {
            throw new Error(Msg + " : 是空值");
        }
    }

    public static void Base_CheckHasValue(String Msg,Integer pValue) {
        if (!Base_HasValue(pValue)) {
            throw new Error(Msg + " : 是空值");
        }
    }

    public static void Base_CheckHasValue(String Msg,Map pValue) {
        if (!Base_HasValue(pValue)) {
            throw new Error(Msg + " : 是空值");
        }
    }

    public static void Base_CheckHasValue(String Msg,List pValue) {
        if (!Base_HasValue(pValue)) {
            throw new Error(Msg + " : 是空值");
        }
    }

    public static void Base_CheckHasValue(String Msg,Double pValue) {
        if (!Base_HasValue(pValue)) {
            throw new Error(Msg + " : 是空值");
        }
    }


    public static boolean Base_HasValue(Integer pValue) {
        Boolean HasValue = true;
        if (pValue == (null)) {
            HasValue = false;
        }
        return HasValue;
    }

    public static boolean Base_HasValue(Map pValue) {
        Boolean HasValue = true;
        if (pValue == (null) || pValue.size() < 1) {
            HasValue = false;
        }
        return HasValue;
    }

    public static boolean Base_HasValue(List pValue) {
        Boolean HasValue = true;
        if (pValue == (null) || pValue.size() < 1) {
            HasValue = false;
        }
        return HasValue;
    }

    public static boolean Base_HasValue(Double pValue) {
        Boolean HasValue = true;
        if (pValue == (null)) {
            HasValue = false;
        }
        return HasValue;
    }


    public static String[] Base_Split(String string, String divisionChar) {
        int i = 0;
        StringTokenizer tokenizer = new StringTokenizer(string, divisionChar);

        String[] str = new String[tokenizer.countTokens()];

        while (tokenizer.hasMoreTokens()) {
            str[i] = new String();
            str[i] = tokenizer.nextToken();
            i++;
        }

        return str;
    }

    public static String Base_ValueOf(Object obj) {
        return (obj == null) ? "null" : obj.toString();

    }

    public static int getRandomInt(int num1,int num2)
    {
        int n=num1+(int)(Math.random()*(num2-num1));
        return n;
    }

    public static String Base_GetDataToMd5(JSONObject jsonObject, String Keys) {
        String[] tmpKeys = Base_Split(Keys, ",");
        String resultMd5 = "";
        for (String key : tmpKeys) {
            String tmpMd5 = md5Hex(Base_ValueOf(jsonObject.get(key)));
            resultMd5 += tmpMd5;
        }
        return resultMd5;
    }

    /**
     * Json 数组按照 key 进行去重
     *
     * @param jsonArray 源Json数组对象
     * @param Keys      按照 key 进行比较 ; 可以采用多个 key 进行比较用 , 作为分隔符。
     * @return
     */
    public static JSONArray Base_DistinctData(JSONArray jsonArray, String Keys) {

        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String resultMd5 = Base_GetDataToMd5(jsonObject, Keys);
            if (!Base_HasValue(map.get(resultMd5))) {
                map.put(resultMd5, jsonObject);
            }
        }

        JSONArray resultArray = new JSONArray();
        Iterator<Map.Entry<String, Object>> entries = map.entrySet().iterator();
        while (entries.hasNext()) {
            Map.Entry<String, Object> entry = entries.next();
            resultArray.add(entry.getValue());
        }
        return resultArray;
    }

    public static Map Base_DistinctData(Map pMap, String Keys) {
        Iterator it = pMap.keySet().iterator();
        while (it.hasNext()) {
            String key;
            String value;
            key = it.next().toString();
            value = (String) pMap.get(key);
            System.out.println(key + "--" + value);
        }
        return null;
    }

    public static Object Base_getDefValue(Object pValue, Object pDefValue) {
        if (Base_HasValue(pValue)) {
            return pValue;
        } else {
            return pDefValue;
        }
    }

    public static String Base_encodeUTF8(String str) {

        try {
            if (Base_HasValue(str)) {
                str = URLDecoder.decode(str, "UTF-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * @param pName  name
     * @param pValue value
     * @return sql条件尾串
     */
    public static String getLocSqlText(String pName, String pValue) {
        String sqlText = String.format(" AND %s ", pName);
        sqlText = String.format(sqlText + " = %s  ", "'" + pValue + "'");
        return sqlText;
    }

    /**
     * 将fromList的数据添加到toList
     *
     * @param toArray   目标list
     * @param fromArray 数据源
     * @return
     */
    public static JSONArray copyJsonArray(JSONArray toArray, JSONArray fromArray) {

        if(Base_HasValue(fromArray)){
            for (int i = 0; i < fromArray.size(); i++) {
                JSONObject o = fromArray.getJSONObject(i);
                toArray.add(o);
            }
        }

        return toArray;
    }

    /**
     * 将fromList的数据添加到toList
     *
     * @param toList   目标list
     * @param fromList 数据源
     * @return
     */
    public static List copyList(List toList,List fromList) {

        if(Base_HasValue(fromList)){
            for (Object o : fromList) {
                fromList.add(o);
            }
        }

        return toList;
    }

    public static void removeDuplicate(List list)  {
        HashSet h  =   new  HashSet(list);
        list.clear();
        list.addAll(h);
    }

    /**
     * 求交集
     * @param list1
     * @param list2
     * @return
     */
    public static List intersection(List list1,List list2){
        List returnList = new ArrayList(list1);
        returnList.retainAll(list2);
        return returnList;
    }

    public static List diffSet(List list1,List list2){
        List returnList = new ArrayList(list1);
        returnList.removeAll(list2);
        return returnList;
    }
}
