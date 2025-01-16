package com.untygames.dersapp;

public class FirebaseData_SinavaKalanSure {
    public String yks_tyt_tarih;
    public String yks_ayt_tarih;
    public String yks_msu_tarih;
    public String yks_lgs_tarih;
    public String yks_kpss_tarih;
    public String yks_kpss_onlisans_tarih;

    public FirebaseData_SinavaKalanSure() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public FirebaseData_SinavaKalanSure(String yks_tyt_tarih, String yks_ayt_tarih, String yks_msu_tarih, String yks_lgs_tarih, String yks_kpss_tarih, String yks_kpss_onlisans_tarih) {
        this.yks_tyt_tarih = yks_tyt_tarih;
        this.yks_ayt_tarih = yks_ayt_tarih;
        this.yks_msu_tarih = yks_msu_tarih;
        this.yks_lgs_tarih = yks_lgs_tarih;
        this.yks_kpss_tarih = yks_kpss_tarih;
        this.yks_kpss_onlisans_tarih = yks_kpss_onlisans_tarih;
    }
}
