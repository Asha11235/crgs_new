package utils;

import java.util.Comparator;

public class SchoolWashComparetor implements Comparator<SchoolComponets> {

@Override
public int compare(SchoolComponets o1, SchoolComponets o2) {
	// TODO Auto-generated method stub
	if( Double.valueOf(o1.getAvg())<Double.valueOf(o2.getAvg())){
		return 1;
	}else{
	return -1;
	}
}
}

