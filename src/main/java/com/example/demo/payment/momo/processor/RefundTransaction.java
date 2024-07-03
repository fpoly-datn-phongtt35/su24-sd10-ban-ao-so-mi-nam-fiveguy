package com.example.demo.payment.momo.processor;

import com.example.demo.payment.momo.config.Environment;
import com.example.demo.payment.momo.enums.Language;
import com.example.demo.payment.momo.models.RefundMoMoRequest;
import com.example.demo.payment.momo.models.RefundMoMoResponse;
import com.example.demo.payment.momo.shared.constants.Parameter;
import com.mservice.shared.exception.MoMoException;
import com.mservice.shared.sharedmodels.HttpResponse;
import com.mservice.shared.utils.Encoder;


public class RefundTransaction extends AbstractProcess<RefundMoMoRequest, RefundMoMoResponse> {
    public RefundTransaction(Environment environment) {
        super(environment);
    }

    public static RefundMoMoResponse process(Environment env, String orderId, String requestId, String amount, Long transId, String description) throws Exception {
        try {
            RefundTransaction m2Processor = new RefundTransaction(env);

            RefundMoMoRequest request = m2Processor.createRefundTransactionRequest(orderId, requestId, amount, transId, description);
            RefundMoMoResponse response = m2Processor.execute(request);

            return response;
        } catch (Exception exception) {
            System.out.println("[RefundTransactionProcess] "+ exception);
        }
        return null;
    }

    @Override
    public RefundMoMoResponse execute(RefundMoMoRequest request) throws MoMoException {
        try {

            String payload = getGson().toJson(request, RefundMoMoRequest.class);

            HttpResponse response = execute.sendToMoMo(environment.getMomoEndpoint().getRefundUrl(), payload);

            if (response.getStatus() != 200) {
                throw new MoMoException("[RefundResponse] [" + request.getOrderId() + "] -> Error API");
            }

            System.out.println("uweryei7rye8wyreow8: "+ response.getData());

            RefundMoMoResponse refundMoMoResponse = getGson().fromJson(response.getData(), RefundMoMoResponse.class);
            String responserawData = Parameter.REQUEST_ID + "=" + refundMoMoResponse.getRequestId() +
                    "&" + Parameter.ORDER_ID + "=" + refundMoMoResponse.getOrderId() +
                    "&" + Parameter.MESSAGE + "=" + refundMoMoResponse.getMessage() +
                    "&" + Parameter.RESULT_CODE + "=" + refundMoMoResponse.getResultCode();

            System.out.println("[RefundResponse] rawData: " + responserawData);

            return refundMoMoResponse;

        } catch (Exception exception) {
            System.out.println("[RefundResponse] "+ exception);
            throw new IllegalArgumentException("Invalid params capture MoMo Request");
        }
    }

    public RefundMoMoRequest createRefundTransactionRequest(String orderId, String requestId, String amount, Long transId, String description) {

        try {
            String requestRawData = new StringBuilder()
                    .append(Parameter.ACCESS_KEY).append("=").append(partnerInfo.getAccessKey()).append("&")
                    .append(Parameter.AMOUNT).append("=").append(amount).append("&")
                    .append(Parameter.DESCRIPTION).append("=").append(description).append("&")
                    .append(Parameter.ORDER_ID).append("=").append(orderId).append("&")
                    .append(Parameter.PARTNER_CODE).append("=").append(partnerInfo.getPartnerCode()).append("&")
                    .append(Parameter.REQUEST_ID).append("=").append(requestId).append("&")
                    .append(Parameter.TRANS_ID).append("=").append(transId)
                    .toString();

            String signRequest = Encoder.signHmacSHA256(requestRawData, partnerInfo.getSecretKey());
            System.out.println("[RefundRequest] rawData: " + requestRawData + ", [Signature] -> " + signRequest);

            return new RefundMoMoRequest(partnerInfo.getPartnerCode(), orderId, requestId, Language.EN, Long.valueOf(amount), transId, signRequest, description);
        } catch (Exception e) {
            System.out.println("[RefundResponse] "+ e);
        }

        return null;
    }
}
