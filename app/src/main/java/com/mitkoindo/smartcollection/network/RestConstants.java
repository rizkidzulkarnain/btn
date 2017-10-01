package com.mitkoindo.smartcollection.network;

/**
 * Created by ericwijaya on 8/20/17.
 */

public final class RestConstants {
    public static final String BASE_URL = "https://mobile-coll.btn.co.id/";
    public static final String BASE_URL_END = "SmartColl";
    public static final String BASE_URL_IMAGE = BASE_URL + BASE_URL_END;

    public static final String ENDPOINT_LOGIN = BASE_URL_END + "/api/Account/login";
    public static final String UPDATE_PASSWORD = BASE_URL_END + "/api/Account/updatePassword";
    public static final String HISTORY_TINDAKAN = BASE_URL_END + "/api/Data/sp";
    public static final String ENDPOINT_LIST_DEBITUR = BASE_URL_END + "/api/Data/sp";
    public static final String ENDPOINT_DETAIL_DEBITUR = BASE_URL_END + "/api/Data/sp";
    public static final String ENDPOINT_VISIT_DROPDOWN = BASE_URL_END + "/api/Data/view";
    public static final String FORM_VISIT = BASE_URL_END + "/api/Data/sp";
    public static final String FORM_CALL = BASE_URL_END + "/api/Data/sp";
    public static final String ENDPOINT_LIST_PHONE_NUMBER = BASE_URL_END + "/api/Data/sp";
    public static final String ENDPOINT_UPLOAD_FILE = BASE_URL_END + "/api/File/upload";
    public static final String DATA_SP = BASE_URL_END + "/api/Data/sp";
    public static final String BUNDLE = BASE_URL_END + "/api/Data/bundle/{dbName}/{limit}";

    public static final String DATABASE_ID_VALUE = "db1";

    public static final String LIST_DEBITUR_SP_NAME = "MKI_SP_DEBITUR_LIST";
    public static final String LIST_DEBITUR_ORDER_BY_VALUE = "C.CU_CIF";
    public static final String LIST_DEBITUR_ORDER_BY_ASSIGN_DATE = "LH.User_Assign_Date";
    public static final String LIST_DEBITUR_ORDER_BY_FULL_NAME = "C.CU_FULLNAME";
    public static final String LIST_DEBITUR_ORDER_BY_TOTAL_KEWAJIBAN = "LD.TOT_Kewajiban";
    public static final String LIST_DEBITUR_STATUS_PENDING_VALUE = "PENDING";
    public static final String LIST_DEBITUR_STATUS_LANCAR_VALUE = "LANCAR";
    public static final String LIST_DEBITUR_STATUS_MATURED_VALUE = "MATURED";
    public static final String LIST_DEBITUR_STATUS_IN_PROGRESS_VALUE = "INPROGRESS";

    public static final String DETAIL_DEBITUR_SP_NAME = "MKI_SP_DEBITUR_DETAIL";

    public static final String DROP_DOWN_PURPOSE_VIEW_NAME = "RFPURPOSE";
    public static final String DROP_DOWN_PURPOSE_SORT_PROPERTY = "P_ID";

    public static final String DROP_DOWN_RELATIONSHIP_VIEW_NAME = "RFRELATIONSHIP";
    public static final String DROP_DOWN_RELATIONSHIP_SORT_PROPERTY = "REL_ID";

    public static final String DROP_DOWN_RESULT_VIEW_NAME = "RFRESULTMASTER";
    public static final String DROP_DOWN_RESULT_SORT_PROPERTY = "RESULT_ID";

    public static final String DROP_DOWN_REASON_VIEW_NAME = "RFREASONMASTER";
    public static final String DROP_DOWN_REASON_SORT_PROPERTY = "REASON_ID";

    public static final String DROP_DOWN_ACTION_VIEW_NAME = "RFACTIONMASTER";
    public static final String DROP_DOWN_ACTION_SORT_PROPERTY = "ACTION_ID";

    public static final String DROP_DOWN_STATUS_AGUNAN_VIEW_NAME = "RFCOLSTATUS";
    public static final String DROP_DOWN_STATUS_AGUNAN_SORT_PROPERTY = "COLSTA_CODE";

    public static final String DROP_DOWN_TELEPON_TYPE_VIEW_NAME = "RFCONTACTTYPE";
    public static final String DROP_DOWN_TELEPON_TYPE_SORT_PROPERTY = "CT_CODE";

    public static final String DROP_DOWN_ADDRESS_TYPE_VIEW_NAME = "RFADDRTYPE";
    public static final String DROP_DOWN_ADDRESS_TYPE_SORT_PROPERTY = "AT_CODE";

    public static final String DROP_DOWN_ADDRESS_VIEW_NAME = "MKI_VW_DEBITUR_ADDRESS_LIST";
    public static final String DROP_DOWN_ADDRESS_SORT_PROPERTY = "ACC_NO";
    public static final String DROP_DOWN_ADDRESS_FILTER_PROPERTY = "ACC_NO";
    public static final String DROP_DOWN_ADDRESS_FILTER_OPERATOR = "eq";

    public static final String FORM_VISIT_SP_NAME = "MKI_SP_SAVE_VISIT_HISTORY";
    public static final String FORM_CALL_SP_NAME = "MKI_SP_SAVE_CALL_HISTORY";

    public static final String LIST_PHONE_NUMBER_SP_NAME = "MKI_SP_DEBITUR_TELFON_LIST";

    public static final String CHECK_IN_SP_NAME = "MKI_SP_STAFF_CHECKIN";

    public static final String TAMBAH_TELEPON_SP_NAME = "MKI_SP_DEBITUR_TAMBAH_TELFON";

    public static final String TAMBAH_ALAMAT_SP_NAME = "MKI_SP_DEBITUR_TAMBAH_ALAMAT";

    public static final String REPORT_DISTRIBUSI_STAFF_SP_NAME = "MKI_SP_REPORT_DISTRIBUSI_DEBITUR_STAFF";

    public static final String REPORT_DISTRIBUSI_SUMMARY_SP_NAME = "MKI_SP_REPORT_DISTRIBUSI_DEBITUR_SUMMARY";

    public static final String STAFF_PRODUCTIVITY_SP_NAME = "MKI_SP_REPORT_STAFF_PRODUCTIVITY";

    public static final String STAFF_PRODUCTIVITY_DEBITUR_SP_NAME = "MKI_SP_REPORT_STAFF_PRODUCTIVITY_DETAIL";

    public static final String AGENT_TRACKING_SP_NAME = "MKI_SP_REPORT_STAFF_TRACKING_LIST";

    public static final String STAFF_DOWNLINE_SP_NAME = "MKI_SP_GET_Downline";

    public static final String STAFF_SP_NAME = "MKI_SP_STAFF_LIST";

    public static final String GALLERY_SP_NAME = "MKI_SP_DEBITUR_GALLERY";

    public static final String ORDER_DIRECTION_ASC_VALUE = "ASC";

    public static final String RESULT_ID_AKAN_SETOR_TANGGAL_VALUE = "R0012";
    public static final String RESULT_ID_AKAN_DATANG_KE_BTN_TANGGAL_VALUE = "R0013";
    public static final String RESULT_ID_MINTA_DIHUBUNGI_TANGGAL_VALUE = "R0016";

    public static final String RELATIONSHIP_ID_YANG_BERSANGKUTAN_VALUE = "00000";
    public static final String RELATIONSHIP_ID_SUAMI_VALUE = "02";
    public static final String RELATIONSHIP_ID_ISTRI_VALUE = "03";

    public static final String STATUS_AGUNAN_ID_RUMAH_KOSONG_VALUE = "01";

    public static final String STAFF_PRODUCTIVITY_CALL_VALUE = "CALL";
    public static final String STAFF_PRODUCTIVITY_VISIT_VALUE = "VISIT";
}
