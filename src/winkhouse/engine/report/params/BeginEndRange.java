package winkhouse.engine.report.params;

public class BeginEndRange {

	private int begin = 0;
	private int end = 0;
	
	public BeginEndRange(int begin, int end){
		this.begin = begin;
		this.end = end;
	}

	public int getBegin() {
		return begin;
	}

	public void setBegin(int begin) {
		this.begin = begin;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}
}
