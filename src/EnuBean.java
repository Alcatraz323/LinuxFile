
import java.util.*;
import java.io.*;
public class EnuBean implements Serializable
{
	private double size;
	private List<EnuBean> child;
	private String abs_dir;
	private EnuBean parent;
	private int depth;
	
	public double getSize(){
		return size;
	}
	public List<EnuBean> getChildren(){
		return child;
	}
	public String getAbsDir(){
		return abs_dir;
	}
	public void setSize(double size){
		this.size=size;
	}
	public void setList(List<EnuBean> child){
		this.child=child;
	}
	public int getDepth(){
		return depth;
	}
	public void setDepth(int depth){
		this.depth=depth;
	}
	public EnuBean getParent(){
		return parent;
	}
	public void setParent(EnuBean parent){
		this.parent=parent;
	}
}
