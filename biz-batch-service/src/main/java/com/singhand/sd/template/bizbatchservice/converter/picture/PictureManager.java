package com.singhand.sd.template.bizbatchservice.converter.picture;

public interface PictureManager {

  String picture(byte[] bytes, String mime, String extName);
}
