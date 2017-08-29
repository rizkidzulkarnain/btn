package com.mitkoindo.smartcollection.network;

/**
 * Created by ericwijaya on 8/20/17.
 */

public final class RestConstants {
    public static final String BASE_URL = "https://btn.ejjv.co:888";

    public static final String ENDPOINT_LOGIN = "/api/Account/login";
    public static final String UPDATE_PASSWORD = "/api/Account/updatePassword";
    public static final String HISTORY_TINDAKAN = "/api/Data/sp";
    public static final String ENDPOINT_LIST_DEBITUR = "/api/Data/sp";
    public static final String ENDPOINT_DETAIL_DEBITUR = "/api/Data/sp";
    public static final String ENDPOINT_VISIT_DROPDOWN = "/api/Data/view";

    public static final String FORM_VISIT = "/api/Data/view";
    public static final String FORM_CALL = "/api/Data/view";
    public static final String ENDPOINT_LIST_PHONE_NUMBER = "/api/Data/view";
    public static final String ENDPOINT_UPLOAD_FILE = "/api/File/upload";


    public static final String DATABASE_ID_VALUE = "db1";

    public static final String LIST_DEBITUR_SP_NAME = "MKI_SP_DEBITUR_LIST";
    public static final String LIST_DEBITUR_ORDER_BY_VALUE = "C.CU_CIF";
    public static final String LIST_DEBITUR_STATUS_PENDING_VALUE = "PENDING";
    public static final String LIST_DEBITUR_STATUS_LANCAR_VALUE = "LANCAR";
    public static final String LIST_DEBITUR_STATUS_MATURED_VALUE = "MATURED";
    public static final String LIST_DEBITUR_STATUS_IN_PROGRESS_VALUE = "IN PROGRESS";

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

    public static final String DROP_DOWN_ADDRESS_VIEW_NAME = "MKI_VW_DEBITUR_ADDRESS_LIST";
    public static final String DROP_DOWN_ADDRESS_SORT_PROPERTY = "ACC_NO";
    public static final String DROP_DOWN_ADDRESS_FILTER_PROPERTY = "ACC_NO";
    public static final String DROP_DOWN_ADDRESS_FILTER_OPERATOR = "eq";

    public static final String FORM_VISIT_SP_NAME = "MKI_SP_SAVE_VISIT_HISTORY";
    public static final String FORM_CALL_SP_NAME = "MKI_SP_SAVE_CALL_HISTORY";

    public static final String LIST_PHONE_NUMBER_SP_NAME = "MKI_SP_DEBITUR_TELFON_LIST";

    public static final String ORDER_DIRECTION_ASC_VALUE = "ASC";
}
