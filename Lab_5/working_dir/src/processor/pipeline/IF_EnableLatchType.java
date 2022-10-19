package processor.pipeline;

public class IF_EnableLatchType {
	boolean IF_enable,IF_busy;
	public IF_EnableLatchType() {
		this.IF_enable = true;
	}
	public boolean isIF_enable() {
		return this.IF_enable;
	}
	public void setIF_enable(boolean IF_enable) {
		this.IF_enable = IF_enable;
	}
	public void setIF_busy(boolean IF_busy) {
		this.IF_busy= IF_busy;
	}
	public boolean isIF_busy() {
		return this.IF_busy;
	}
}
