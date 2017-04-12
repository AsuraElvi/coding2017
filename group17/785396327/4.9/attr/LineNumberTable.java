package attr;

import iterator.ByteCodeIterator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IBM on 2017/4/12.
 */
public class LineNumberTable extends AttributeInfo {
    List<LineNumberItem> items = new ArrayList<LineNumberItem>();

    private static class LineNumberItem{
        int startPC;
        int lineNum;
        public int getStartPC() {
            return startPC;
        }
        public void setStartPC(int startPC) {
            this.startPC = startPC;
        }
        public int getLineNum() {
            return lineNum;
        }
        public void setLineNum(int lineNum) {
            this.lineNum = lineNum;
        }
    }
    public void addLineNumberItem(LineNumberItem item){
        this.items.add(item);
    }
    public LineNumberTable(int attrNameIndex, int attrLen) {
        super(attrNameIndex, attrLen);

    }

    public static LineNumberTable parse(ByteCodeIterator iter){

        return null;
    }


}
