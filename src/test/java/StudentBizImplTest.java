import com.yc.AppConfig;
import com.yc.biz.StudentBizImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @program: testSpring
 * @description:
 * @author: oyx
 * @create: 2021-04-04 17:11
 */
public class StudentBizImplTest {
    ApplicationContext ac;
    private StudentBizImpl studentBiz;

    @Before
    public void setUp()
    {
        ac=new AnnotationConfigApplicationContext(AppConfig.class);
        studentBiz= (StudentBizImpl) ac.getBean("studentBizImpl");
    }

    @Test
    public void testAdd()
    {
        studentBiz.add("李四");

    }

    @Test
    public void testUpdate()
    {
        studentBiz.update("李四");
    }
}
