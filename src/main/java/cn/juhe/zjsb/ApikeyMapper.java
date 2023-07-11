package cn.juhe.zjsb;


//应该是UserMapper
public interface ApikeyMapper {
    String select();

    void updateApikey(String appkey);

    String selectRunmodel();

    void updateRunModel(String runmodel);
}

