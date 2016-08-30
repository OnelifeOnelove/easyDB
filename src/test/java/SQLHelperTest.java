import com.xyh.easyDB.helper.ReflectHelper;
import com.xyh.easyDB.model.Data;
import com.xyh.easyDB.helper.DBHelper;
import com.xyh.easyDB.helper.SQLHelper;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiayuhui on 2016/8/26.
 */
public class SQLHelperTest {

    @Test
    public void testInsert() throws Exception {
        SQLHelper helper = new SQLHelper(DBHelper.initDataSource());

        Data data = new Data();
        data.setName("nono");
        data.setCreateTime(new Date());
        int count = helper.insert(data);
        System.out.println("insert count is " + count);
        System.out.println("obj id is " + data.getId());
    }

    @Test
    public void testDelete() throws Exception {
        SQLHelper helper = new SQLHelper(DBHelper.initDataSource());

        Data data = new Data();
        data.setId(6);
        int count = helper.delete(data);
        System.out.println(count);
    }

    @Test
    public void testUpdate() throws Exception {
        SQLHelper helper = new SQLHelper(DBHelper.initDataSource());

        Data data = new Data();
        data.setId(5);
        data.setType(0);
        data.setTps(200);
        int count = helper.update(data);
        System.out.println(count);
    }

    @Test
    public void testQuery() throws Exception {
        SQLHelper helper = new SQLHelper(DBHelper.initDataSource());
        Data data = helper.getEntity(1, Data.class);
        System.out.println(data);
    }

    @Test
    public void testQueryMap() throws Exception {
        SQLHelper helper = new SQLHelper(DBHelper.initDataSource());
        Map<String, Object> map = helper.get(1, Data.class);
        System.out.println(map);
    }

    @Test
    public void testReflect() throws Exception {
        try {
            ReflectHelper helper = new ReflectHelper();
            Data data = new Data();
            data.setId(6);
            data.setName("nono");
            data.setCreateTime(new Date());
            Object id = helper.getId(data);
            System.out.println("id is " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tempTest() throws Exception {
        Data data = new Data();
        Class clazz = data.getClass();
        Method m = data.getClass().getMethod("setId", new Class[]{Integer.class});
        System.out.println(m);
    }

}
