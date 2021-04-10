import com.yc.MyAppConfig;
import com.yc.biz.StudentBiz;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: testSpring
 * @description:
 * @author: oyx
 * @create: 2021-04-09 19:08
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MyAppConfig.class)
public class StudentBizImplTest {
    @Autowired
    private StudentBiz sbi;
    @Test
    public void testAdd()
    {
        sbi.add("李四");

    }

    @Test
    public void testUpdate()
    {
        sbi.update("李四");
    }

    @Test
    public void testFind()
    {
        sbi.find("李四");
    }

}
