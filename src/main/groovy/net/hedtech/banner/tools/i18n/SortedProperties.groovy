/*******************************************************************************
 * Copyright 2013-2019 Ellucian Company L.P. and its affiliates.
 ******************************************************************************/
package net.hedtech.banner.tools.i18n

public class SortedProperties extends Properties {
    public Enumeration keys() {
        Enumeration keysEnum = super.keys()
        ArrayList<String> keyList = Collections.synchronizedList(new ArrayList<String>())
        while(keysEnum.hasMoreElements()){
            keyList.add((String)keysEnum.nextElement())
        }
        Collections.sort(keyList)
        return Collections.enumeration(keyList)
    }

}