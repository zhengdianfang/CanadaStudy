package com.canadian.study.common;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.canadian.study.R;
import com.canadian.study.bean.Booth;
import com.canadian.study.bean.Message;
import com.canadian.study.bean.SchoolCity;
import com.canadian.study.bean.University;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by zheng on 16/9/8.
 */

public class DatasUtils {

    public static List<SchoolCity> sSchoolCities = null;
    public static List<University> sUniversities = null;

    public static List<Booth> readBoothData(Context context, int rawResId){

        List<Booth> booths = new ArrayList<Booth>();
        InputStream in =  context.getResources().openRawResource(rawResId);
        InputStreamReader isr = new InputStreamReader(in);
        BufferedReader br = new BufferedReader(isr);
        String s = null;

        String city = context.getString(R.string.beijing);
        switch (rawResId){
            case R.raw.bejing:
                city = context.getString(R.string.beijing);
                break;
            case R.raw.shanghai:
                city = context.getString(R.string.shanghai);
                break;
            case R.raw.zhengzhou:
                city = context.getString(R.string.zhengzhou);
                break;
            case R.raw.wuhan:
                city = context.getString(R.string.wuhan);
                break;
            case R.raw.nanjing:
                city = context.getString(R.string.nanjin);
                break;
            case R.raw.guangzhou:
                city = context.getString(R.string.guangzhou);
                break;
            case R.raw.chongqing:
                city = context.getString(R.string.chongqing);
                break;
            case R.raw.chengdu:
                city = context.getString(R.string.chengdu);
                break;
            case R.raw.xian:
                city = context.getString(R.string.xian);
                break;
        }

        try {
            while ((s = br.readLine()) != null){
                Booth booth = parseBoothObj(s, city);
                if(null != booth){
                    booths.add(booth);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
                isr.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return booths;
    }

    public static void readSchoolBooth(Context context){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<SchoolCity> all = realm.where(SchoolCity.class).findAll();
        if (all == null || all.isEmpty()) {
            List<SchoolCity> schoolCities = new ArrayList<>();
            InputStream in = context.getResources().openRawResource(R.raw.schoolbooth);
            InputStreamReader isr = null;
            try {
                isr = new InputStreamReader(in, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            BufferedReader bis = new BufferedReader(isr);
            String s;
            try {
                while ((s = bis.readLine()) != null){
                    List<SchoolCity> schoolCityList = parseReadSchoolCityLineStr(s);
                    if (null != schoolCityList){
                        schoolCities.addAll(schoolCityList);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    bis.close();
                    isr.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            sSchoolCities = schoolCities;
            realm.beginTransaction();
            for (int i = 0; i < schoolCities.size(); i++) {
                SchoolCity schoolCity = schoolCities.get(i);
                schoolCity.id = i;
                realm.copyToRealmOrUpdate(schoolCity);
            }
            realm.commitTransaction();
        }else {
            sSchoolCities = realm.copyFromRealm(all);
        }

    }


    private static List<SchoolCity> parseReadSchoolCityLineStr(String s){
        List<SchoolCity> schoolCityList = null;
        SchoolCity schoolCity;
        if (!TextUtils.isEmpty(s)){
            schoolCityList = new ArrayList<>();
            String[] datas = s.split("###");
            String universityName = datas[0];
            s = s.substring(s.indexOf("###") + "###".length());
            datas = s.split("###");
            if (null != datas){
                int zu = datas.length / 2;
                int index = 0;
                for(int i=1 ; i<=zu; i++){
                    schoolCity = new SchoolCity();
                    schoolCity.universityName = universityName.trim();
                    schoolCity.joinCity = datas[index].trim();
                    schoolCity.siteNum = datas[index + 1].trim();
                    schoolCityList.add(schoolCity);
                    index += 2;
                }
            }
        }

        return schoolCityList;
    }


    public static void readSchoolData2Db(Context context){
        copySchoolZipFile(context);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<University> all = realm.where(University.class).findAll().sort("pingyingIndex");
        if (all == null || all.isEmpty()){
            List<University> universityList = new ArrayList<University>();
            InputStream in = context.getResources().openRawResource(R.raw.schooldata);
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(isr);
            String s;
            int sum = 0;
            try {
                while ((s = br.readLine()) != null){
                    University university = parseReadLineStr(s);
                    if (null != university){
                        ++sum;
                        university.id = sum;
                        university.picPaths = getUniversityPics(context, university);
                        RealmResults<SchoolCity> schoolCities = realm.where(SchoolCity.class).equalTo("universityName", university.chineseName).findAll();
                        university.isJoinShow = schoolCities.size() > 0;
                        university.schoolCities = new RealmList<>();
                        university.schoolCities.addAll(schoolCities);
                        if (null != university){
                            universityList.add(university);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    br.close();
                    isr.close();
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(universityList);
            realm.commitTransaction();
            all = realm.where(University.class).findAll().sort("pingyingIndex");
        }
        sUniversities = realm.copyFromRealm(all);

    }

    static void copySchoolZipFile(Context context) {
        try {
            File file = new File(context.getCacheDir(), "schoolPics.zip");
            if (!file.exists()){
                ZdfFileUtils.copyFile(context, context.getResources().openRawResource(R.raw.school_pics), file);
                File unZipDir = new File(context.getCacheDir().getAbsolutePath());
                unZipDir.mkdirs();
                upZipFile(file, unZipDir.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static University parseReadLineStr(String s)throws Exception{
        University university = null;
        if (!TextUtils.isEmpty(s)){
            String[] datas = s.split("###");
            if (null != datas){
                university = new University();
                for (int i=0; i<datas.length; i++){
                    String c = datas[i].trim();
                    switch (i){
                        case 0:
                            university.englishName = c;
                            String n = c.substring(0, 1);
                            //发现第一学院名字前面有一个隐藏字符 无法删除 所以做些特殊处理
                            if (c.indexOf("Red River College") > 0){
                                n = "R";
                            }
                            university.pingyingIndex = n.toLowerCase();
                            break;
                        case 1:
                            university.chineseName = c ;
                            break;
                        case 2:
                            university.zone = c;
                            break;
                        case 3:
                            university.type = c;
                            break;
                        case 4:
                            university.email = c;
                            break;
                        case 5:
                            university.phonenum = c ;
                            break;
                        case 6:
                            university.netUrl = c ;
                            break;
                        case 7:
                            university.detail = c;
                            break;
                    }
                }
            }
        }

        return university;
    }


    private static String getUniversityPics(Context context, University university){
        StringBuffer sb = new StringBuffer();
        if (university != null) {
            File file = new File(context.getCacheDir() + "/school_pics/");
            if (file.exists()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        Log.d("======", "file name : "+ f.getName());
                        if (f.getName().equals(String.valueOf(university.id))) {
                            if (university.id == 235) {
                                File[] files1 = f.listFiles();
                                Log.d("======", "235 dir files count : " + files1.length);
                            }
                            File[] files1 = f.listFiles();
                            if (files1 != null) {
                                for (File file1 : files1) {
                                    if (file1.isFile() && (file1.getName().toLowerCase().endsWith("jpg") || file1.getName().toLowerCase().endsWith("png"))) {
                                        sb.append(";;").append(file1.getAbsolutePath());
                                    }
                                }
                                sb.delete(0, 2);
                            }
                        }
                    }
                }
            }
        }
        return sb.toString();
    }

    private static Booth parseBoothObj(String s, String city){
        if(!TextUtils.isEmpty(s)){

            String[] arr = s.split("###");
            Booth booth = new Booth();
            booth.mark = arr[0];
            String[] aa = arr[1].split(",");
            booth.x = Integer.parseInt(aa[0].trim());
            booth.y = Integer.parseInt(aa[1].trim());
            booth.width = (Integer.parseInt(aa[2].trim()));
            booth.height = (Integer.parseInt(aa[3].trim()));
            booth.joinCity = city;
            return booth;
        }
        return null;
    }

    public static void readNewMsg2Db(Context context){
        Realm realm = Realm.getDefaultInstance();
        InputStream in =  context.getResources().openRawResource(R.raw.new_msg);
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(in, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(isr);
        String s;
        List<Message> messages = new ArrayList<Message>();
        int id = 1;
        realm.beginTransaction();
        try {
            while ((s = br.readLine()) != null){
                if(!TextUtils.isEmpty(s)){
                    Message message = new Message();
                    message.id = id;
                    message.title = s.substring(1, s.indexOf("]"));
                    message.content = s;
                    message.time = System.currentTimeMillis();
                    messages.add(message);
                    realm.copyToRealm(message);
                    ++id;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
                isr.close();
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        realm.commitTransaction();

    }

    public static int upZipFile(File zipFile, String folderPath)throws ZipException,IOException {
        //public static void upZipFile() throws Exception{
        ZipFile zfile=new ZipFile(zipFile);
        Enumeration zList=zfile.entries();
        ZipEntry ze=null;
        byte[] buf=new byte[1024];
        while(zList.hasMoreElements()){
            ze=(ZipEntry)zList.nextElement();
            if(ze.isDirectory()){
                String dirstr = folderPath + ze.getName();
                //dirstr.trim();
                dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                File f=new File(dirstr);
                f.mkdir();
                continue;
            }
            OutputStream os=new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
            InputStream is=new BufferedInputStream(zfile.getInputStream(ze));
            int readLen=0;
            while ((readLen=is.read(buf, 0, 1024))!=-1) {
                os.write(buf, 0, readLen);
            }
            is.close();
            os.close();
        }
        zfile.close();
        return 0;
    }

    public static File getRealFileName(String baseDir, String absFileName){
        String[] dirs=absFileName.split("/");
        File ret=new File(baseDir);
        String substr = null;
        if(dirs.length>1){
            for (int i = 0; i < dirs.length-1;i++) {
                substr = dirs[i];
                try {
                    //substr.trim();
                    substr = new String(substr.getBytes("8859_1"), "GB2312");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ret=new File(ret, substr);

            }
            if(!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length-1];
            try {
                //substr.trim();
                substr = new String(substr.getBytes("8859_1"), "GB2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            ret=new File(ret, substr);
            return ret;
        }
        return ret;
    }

    public static boolean deleteDir(File dir) {

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so now it can be smoked
        return dir.delete();
    }


}
