package test;

import clz.ClassFile;
import clz.ClassIndex;
import constant.*;
import field.Field;
import jvm_1.ClassFileLoader;
import method.Method;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Created by IBM on 2017/4/10.
 */
public class ClassFileloaderTest {
    private static final String FULL_QUALIFIED_CLASS_NAME = "jvm_1/EmployeeV1";

    static String path1 = "G:\\Git\\homework\\coding2017\\group17\\785396327\\3.12\\out\\production\\785396327";
    static String path2 = "C:\temp";

    static ClassFile clzFile = null;
    static {
        ClassFileLoader loader = new ClassFileLoader();
        loader.addClassPath(path1);
        String className = "jvm_1.EmployeeV1";

        clzFile = loader.loadClass(className);
//        clzFile.print();
    }


    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testClassPath(){

        ClassFileLoader loader = new ClassFileLoader();
        loader.addClassPath(path1);
        loader.addClassPath(path2);

        String clzPath = loader.getClassPath();

        Assert.assertEquals(path1+";"+path2,clzPath);

    }

    @Test
    public void testClassFileLength() {

        ClassFileLoader loader = new ClassFileLoader();
        loader.addClassPath(path1);

        String className = "jvm_1.EmployeeV1";

        byte[] byteCodes = loader.readBinaryCode(className);

        // 注意：这个字节数可能和你的JVM版本有关系， 你可以看看编译好的类到底有多大
        Assert.assertEquals(1020, byteCodes.length);

    }


    @Test
    public void testMagicNumber(){
        ClassFileLoader loader = new ClassFileLoader();
        loader.addClassPath(path1);
        String className = "com.coderising.jvm.test.EmployeeV1";
        byte[] byteCodes = loader.readBinaryCode(className);
        byte[] codes = new byte[]{byteCodes[0],byteCodes[1],byteCodes[2],byteCodes[3]};


        String acctualValue = this.byteToHexString(codes);

        Assert.assertEquals("cafebabe", acctualValue);
    }



    private String byteToHexString(byte[] codes ){
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<codes.length;i++){
            byte b = codes[i];
            int value = b & 0xFF;
            String strHex = Integer.toHexString(value);
            if(strHex.length()< 2){
                strHex = "0" + strHex;
            }
            buffer.append(strHex);
        }
        return buffer.toString();
    }

    /**
     * ----------------------------------------------------------------------
     */


    @Test
    public void testVersion(){

        Assert.assertEquals(0, clzFile.getMinorVersion());
        Assert.assertEquals(51, clzFile.getMajorVersion());

    }

    @Test
    public void testConstantPool(){


        ConstantPool pool = clzFile.getConstantPool();

        Assert.assertEquals(53, pool.getSize());

        {
            ClassInfo clzInfo = (ClassInfo) pool.getConstantInfo(7);
            Assert.assertEquals(44, clzInfo.getUtf8Index());

            UTF8Info utf8Info = (UTF8Info) pool.getConstantInfo(44);
            Assert.assertEquals(FULL_QUALIFIED_CLASS_NAME, utf8Info.getValue());
        }
        {
            ClassInfo clzInfo = (ClassInfo) pool.getConstantInfo(11);
            Assert.assertEquals(48, clzInfo.getUtf8Index());

            UTF8Info utf8Info = (UTF8Info) pool.getConstantInfo(48);
            Assert.assertEquals("java/lang/Object", utf8Info.getValue());
        }
        {
            UTF8Info utf8Info = (UTF8Info) pool.getConstantInfo(12);
            Assert.assertEquals("name", utf8Info.getValue());

            utf8Info = (UTF8Info) pool.getConstantInfo(13);
            Assert.assertEquals("Ljava/lang/String;", utf8Info.getValue());

            utf8Info = (UTF8Info) pool.getConstantInfo(14);
            Assert.assertEquals("age", utf8Info.getValue());

            utf8Info = (UTF8Info) pool.getConstantInfo(15);
            Assert.assertEquals("I", utf8Info.getValue());

            utf8Info = (UTF8Info) pool.getConstantInfo(16);
            Assert.assertEquals("<init>", utf8Info.getValue());

            utf8Info = (UTF8Info) pool.getConstantInfo(17);
            Assert.assertEquals("(Ljava/lang/String;I)V", utf8Info.getValue());

            utf8Info = (UTF8Info) pool.getConstantInfo(18);
            Assert.assertEquals("Code", utf8Info.getValue());
        }

        {
            MethodRefInfo methodRef = (MethodRefInfo)pool.getConstantInfo(1);
            Assert.assertEquals(11, methodRef.getClassInfoIndex());
            Assert.assertEquals(36, methodRef.getNameAndTypeIndex());
        }

        {
            NameAndTypeInfo nameAndType = (NameAndTypeInfo) pool.getConstantInfo(13);
            Assert.assertEquals(9, nameAndType.getIndex1());
            Assert.assertEquals(14, nameAndType.getIndex2());
        }
        //抽查几个吧
        {
            MethodRefInfo methodRef = (MethodRefInfo)pool.getConstantInfo(45);
            Assert.assertEquals(1, methodRef.getClassInfoIndex());
            Assert.assertEquals(46, methodRef.getNameAndTypeIndex());
        }

        {
            UTF8Info utf8Info = (UTF8Info) pool.getConstantInfo(53);
            Assert.assertEquals("EmployeeV1.java", utf8Info.getValue());
        }
    }
    @Test
    public void testClassIndex(){

        ClassIndex clzIndex = clzFile.getClzIndex();
        ClassInfo thisClassInfo = (ClassInfo)clzFile.getConstantPool().getConstantInfo(clzIndex.getThisClassIndex());
        ClassInfo superClassInfo = (ClassInfo)clzFile.getConstantPool().getConstantInfo(clzIndex.getSuperClassIndex());


        Assert.assertEquals(FULL_QUALIFIED_CLASS_NAME, thisClassInfo.getClassName());
        Assert.assertEquals("java/lang/Object", superClassInfo.getClassName());
    }

    /**
     * 下面是第三次JVM课应实现的测试用例
     */
    @Test
    public void testReadFields(){

        List<Field> fields = clzFile.getFields();
        Assert.assertEquals(2, fields.size());
        {
            Field f = fields.get(0);
            Assert.assertEquals("name:Ljava/lang/String;", f.toString());
        }
        {
            Field f = fields.get(1);
            Assert.assertEquals("age:I", f.toString());
        }
    }
    @Test
    public void testMethods(){

        List<Method> methods = clzFile.getMethods();
        ConstantPool pool = clzFile.getConstantPool();

        {
            Method m = methods.get(0);
            assertMethodEquals(pool,m,
                    "<init>",
                    "(Ljava/lang/String;I)V",
                    "2ab7000c2a2bb5000f2a1cb50011b1");

        }
        {
            Method m = methods.get(1);
            assertMethodEquals(pool,m,
                    "setName",
                    "(Ljava/lang/String;)V",
                    "2a2bb5000fb1");

        }
        {
            Method m = methods.get(2);
            assertMethodEquals(pool,m,
                    "setAge",
                    "(I)V",
                    "2a1bb50011b1");
        }
        {
            Method m = methods.get(3);
            assertMethodEquals(pool,m,
                    "sayHello",
                    "()V",
                    "b2001c1222b60024b1");

        }
        {
            Method m = methods.get(4);
            assertMethodEquals(pool,m,
                    "main",
                    "([Ljava/lang/String;)V",
                    "bb000159122b101db7002d4c2bb6002fb1");
        }
    }

    private void assertMethodEquals(ConstantPool pool,Method m , String expectedName, String expectedDesc,String expectedCode){
        String methodName = pool.getUTF8String(m.getNameIndex());
        String methodDesc = pool.getUTF8String(m.getDescriptorIndex());
        String code = m.getCodeAttr().getCode();
        Assert.assertEquals(expectedName, methodName);
        Assert.assertEquals(expectedDesc, methodDesc);
        Assert.assertEquals(expectedCode, code);
    }

}
