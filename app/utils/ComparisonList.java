package utils;

import java.util.ArrayList;

public class ComparisonList {

	ArrayList<Comparison> comparisonlist;

	public ComparisonList() {
		comparisonlist = new ArrayList<Comparison>();
	}

	public void addComparison(Comparison comp) {
		comparisonlist.add(comp);
	}

	public ArrayList<Comparison> getComparison(String formName) {
		ArrayList<Comparison> comps = new ArrayList<Comparison>();
		for (Comparison c : comparisonlist) {
			if (c.baseFormName.equals(formName))
				comps.add(c);
		}
		if (comps.size() > 0)
			return comps;

		return null;
	}

}
