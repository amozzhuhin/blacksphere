package edu.nops.blacksphere.ide.bundle;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

/** Конвертер XStream, переводящий строки */
public class BundleConverter implements Converter {

	@Override
	public void marshal(Object arg0, HierarchicalStreamWriter arg1,
			MarshallingContext arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object unmarshal(HierarchicalStreamReader arg0,
			UnmarshallingContext arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canConvert(Class type) {
		System.out.println(type.toString());
		return type.equals(String.class);
	}

}
