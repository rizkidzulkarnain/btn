package com.mitkoindo.smartcollection.objectdata;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by W8 on 26/09/2017.
 */

public class LaporanCall
{
    public String TanggalCall;
    public String NomorRekening;
    public String NamaLengkap;
    public String TujuanVisit;
    public String TujuanVisitDesc;
    public String StatusAgunan;
    public String StatusAgunanDesc;
    public String KondisiAgunan;
    public String AlasanTidakBayar;
    public String AlasanTidakBayarDesc;
    public String DeskripsiAlasanTidakBayar;
    public String OrangYangDikunjungi;
    public String HubunganYangDikunjungi;
    public String HubunganYangDikunjungiDesc;
    public String TindakLanjut;
    public String TindakLanjutDesc;
    public String TanggalTindakLanjut;
    public String HasilVisit;
    public String HasilVisitDesc;
    public String TanggalJanjiBayar;
    public String NominalJanjiBayar;
    public String Catatan;

    //extract data
    public void ParseData(JSONObject dataObject)
    {
        try
        {
            TanggalCall = dataObject.getString("TanggalCall");
            NomorRekening = dataObject.getString("NomorRekening");
            NamaLengkap = dataObject.getString("NamaLengkap");
            TujuanVisit = dataObject.getString("TujuanVisit");
            TujuanVisitDesc = dataObject.getString("TujuanVisitDesc");
            StatusAgunan = dataObject.getString("StatusAgunan");
            StatusAgunanDesc = dataObject.getString("StatusAgunanDesc");
            KondisiAgunan = dataObject.getString("KondisiAgunan");
            AlasanTidakBayar = dataObject.getString("AlasanTidakBayar");
            AlasanTidakBayarDesc = dataObject.getString("AlasanTidakBayarDesc");
            DeskripsiAlasanTidakBayar = dataObject.getString("DeskripsiAlasanTidakBayar");
            OrangYangDikunjungi = dataObject.getString("OrangYangDikunjungi");
            HubunganYangDikunjungi = dataObject.getString("HubunganYangDikunjungi");
            HubunganYangDikunjungiDesc = dataObject.getString("HubunganYangDikunjungiDesc");
            TindakLanjut = dataObject.getString("TindakLanjut");
            TindakLanjutDesc = dataObject.getString("TindakLanjutDesc");
            TanggalTindakLanjut = dataObject.getString("TanggalTindakLanjut");
            HasilVisit = dataObject.getString("HasilVisit");
            HasilVisitDesc = dataObject.getString("HasilVisitDesc");
            TanggalJanjiBayar = dataObject.getString("TanggalJanjiBayar");
            NominalJanjiBayar = dataObject.getString("NominalJanjiBayar");
            Catatan = dataObject.getString("Catatan");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
