package processor.pipeline;

public class IF_EnableLatchType {
	
	boolean IFenable;
	
	// constructors 
	public IF_EnableLatchType() {
		this.IFenable = true;
	}
	public IF_EnableLatchType(boolean IFenable) {
		this.IFenable = IFenable;
	}

	// getter and setter for IFenable boolean variable
	public boolean isIF_enable() {
		return this.IFenable;
	}
	public void setIF_enable(boolean IFenable) {
		this.IFenable = IFenable;
	}
}
