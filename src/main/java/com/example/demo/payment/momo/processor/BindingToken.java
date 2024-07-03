package com.example.demo.payment.momo.processor;


import com.example.demo.payment.momo.config.Environment;
import com.example.demo.payment.momo.enums.Language;
import com.example.demo.payment.momo.models.BindingTokenRequest;
import com.example.demo.payment.momo.models.BindingTokenResponse;
import com.example.demo.payment.momo.shared.constants.Parameter;
import com.example.demo.payment.momo.shared.utils.Encoder;
import com.mservice.shared.exception.MoMoException;
import com.mservice.shared.sharedmodels.HttpResponse;

public class BindingToken extends AbstractProcess<BindingTokenRequest, BindingTokenResponse> {
    public BindingToken(Environment environment) {
        super(environment);
    }

    public static BindingTokenResponse process(Environment env, String orderId, String requestId, String partnerClientId, String callbackToken) {
        try {
            BindingToken m2Processor = new BindingToken(env);

            BindingTokenRequest request = m2Processor.createBindingTokenRequest(orderId, requestId, partnerClientId, callbackToken);
            BindingTokenResponse bindingTokenResponse = m2Processor.execute(request);

            return bindingTokenResponse;
        } catch (Exception exception) {
        }
        return null;
    }

    @Override
    public BindingTokenResponse execute(BindingTokenRequest request) throws MoMoException {
        try {

            String payload = getGson().toJson(request, BindingTokenRequest.class);

            HttpResponse response = execute.sendToMoMo(environment.getMomoEndpoint().getTokenBindUrl(), payload);

            if (response.getStatus() != 200) {
                throw new MoMoException("[BindingTokenResponse] [" + request.getOrderId() + "] -> Error API");
            }


            BindingTokenResponse BindingTokenResponse = getGson().fromJson(response.getData(), BindingTokenResponse.class);
            String responserawData = Parameter.REQUEST_ID + "=" + BindingTokenResponse.getRequestId() +
                    "&" + Parameter.ORDER_ID + "=" + BindingTokenResponse.getOrderId() +
                    "&" + Parameter.MESSAGE + "=" + BindingTokenResponse.getMessage() +
                    "&" + Parameter.RESULT_CODE + "=" + BindingTokenResponse.getResultCode();


            return BindingTokenResponse;

        } catch (Exception exception) {
            System.out.println("[BindingTokenResponse] "+ exception);
            throw new IllegalArgumentException("Invalid params capture MoMo Request");
        }
    }

    public BindingTokenRequest createBindingTokenRequest(String orderId, String requestId, String partnerClientId, String callbackToken) {

        try {
            String requestRawData = new StringBuilder()
                    .append(Parameter.ACCESS_KEY).append("=").append(partnerInfo.getAccessKey()).append("&")
                    .append(Parameter.CALLBACK_TOKEN).append("=").append(callbackToken).append("&")
                    .append(Parameter.ORDER_ID).append("=").append(orderId).append("&")
                    .append(Parameter.PARTNER_CLIENT_ID).append("=").append(partnerClientId).append("&")
                    .append(Parameter.PARTNER_CODE).append("=").append(partnerInfo.getPartnerCode()).append("&")
                    .append(Parameter.REQUEST_ID).append("=").append(requestId)
                    .toString();

            String signRequest = Encoder.signHmacSHA256(requestRawData, partnerInfo.getSecretKey());
            System.out.println("[BindingTokenRequest] rawData: " + requestRawData + ", [Signature] -> " + signRequest);

            return new BindingTokenRequest(partnerInfo.getPartnerCode(), orderId, requestId, Language.EN, partnerClientId, callbackToken, signRequest);
        } catch (Exception e) {
            System.out.println("[BindingTokenResponse] "+ e);
        }

        return null;
    }

}
