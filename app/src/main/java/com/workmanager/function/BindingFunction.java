package com.workmanager.function;

import com.workmanager.bean.AssociateBean;
import com.workmanager.entity.DryingEntity;
import com.workmanager.entity.FieldEntity;
import com.workmanager.entity.WeightEntity;

public class BindingFunction {
    public static void fieldUpdate(AssociateBean ass){
        for (FieldEntity f :GetDataBase.fieldList){
            if (f.getThingId()==ass.getThingId()){
                f.setNcfTagId(ass.getNfcTagId());
            }
        }
    }

    public static void fieldDelete(AssociateBean ass){
        for (FieldEntity f :GetDataBase.fieldList){
            if (f.getThingId()==ass.getThingId()){
                f.setNcfTagId("");
            }
        }
    }

    public static void dryUpdate(AssociateBean ass){
        for (DryingEntity d :GetDataBase.dryingList){
            if (d.getThingId()==ass.getThingId()){
                d.setNcfTagId(ass.getNfcTagId());
            }
        }
    }

    public static void dryDelete(AssociateBean ass){
        for (DryingEntity d :GetDataBase.dryingList){
            if (d.getThingId()==ass.getThingId()){
                d.setNcfTagId("");
            }
        }
    }

    public static void weightUpdate(AssociateBean ass){
        for (WeightEntity w :GetDataBase.weightList){
            if (w.getThingId()==ass.getThingId()){
                w.setNcfTagId(ass.getNfcTagId());
            }
        }
    }

    public static void weightDelete(AssociateBean ass){
        for (WeightEntity w :GetDataBase.weightList){
            if (w.getThingId()==ass.getThingId()){
                w.setNcfTagId("");
            }
        }
    }
}
