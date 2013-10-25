package com.edian.www.util;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class ChoiceOnClickListener implements OnClickListener {

    private int which = 0;  
    @Override  
    public void onClick(DialogInterface dialogInterface, int which) {  
        this.which = which;  
    }  
      
    public int getWhich() {  
        return which;  
    } 

}
