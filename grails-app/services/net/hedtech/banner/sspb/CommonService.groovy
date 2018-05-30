package net.hedtech.banner.sspb

import grails.transaction.Transactional
import org.apache.commons.codec.binary.Base64


class CommonService {

   public static decodeBase64(Map params){
       Map parameter = [:]
       if(params.get("encoded")) {
           for (param in params) {
               if (!(param.key.equals("pluralizedResourceName") || param.key.equals("action")
                       || param.key.equals("controller")|| param.key.equals("encoded"))) {
                   String keyBase = "${param.key}"
                   String valBase = "${param.value}"
                   if(keyBase.length()>4){
                       keyBase = keyBase.substring(4)
                       valBase = valBase.substring(4)
                   }
                   byte[] key = Base64.decodeBase64(keyBase);
                   if (valBase.equalsIgnoreCase("undefined")|| valBase.equalsIgnoreCase("null")){
                       parameter.put(new String(key), null)
                   }else{
                        byte[] value = Base64.decodeBase64(valBase)
                       parameter.put(new String(key), new String(value))
                   }
               }
           }
       }
       return parameter;
   }
}
