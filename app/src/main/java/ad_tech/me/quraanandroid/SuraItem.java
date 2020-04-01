package ad_tech.me.quraanandroid;

/**
 * Created by adel_2 on 2016-10-15.
 */

public class SuraItem {
    String SuraID;
    String SuraName;
    String SuraName_en;
    String PageNo;
    int AayaCount;
    int WordsCount;
    String Place;

    public String getSuraID() {
        return SuraID;
    }

    public void setSuraID(String suraID) {
        SuraID = suraID;
    }

    public String getSuraName() {
        return SuraName;
    }

    public void setSuraName(String suraName) {
        SuraName = suraName;
    }

    public String getSuraName_en() {
        return SuraName_en;
    }

    public void setSuraName_en(String suraName_en) {
        SuraName_en = suraName_en;
    }

    public String getPageNo() {
        return PageNo;
    }

    public void setPageNo(String pageNo) {
        PageNo = pageNo;
    }

    public int getAayaCount() {
        return AayaCount;
    }

    public void setAayaCount(int aayaCount) {
        AayaCount = aayaCount;
    }

    public int getWordsCount() {
        return WordsCount;
    }

    public void setWordsCount(int wordsCount) {
        WordsCount = wordsCount;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }
}
