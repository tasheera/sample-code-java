package net.authorize.sample.acceptsuite;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Logger;

import net.authorize.Environment;
import net.authorize.api.contract.v1.*;
import net.authorize.api.controller.base.ApiOperationBase;
import net.authorize.api.controller.GetHostedPaymentPageController;

public class GetAnAcceptPaymentPage {

    private static final Logger LOGGER = Logger.getLogger(GetAnAcceptPaymentPage.class.getName());

    private GetAnAcceptPaymentPage(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
	
	public static ANetApiResponse run(String apiLoginId, String transactionKey, Double amount) {

        ApiOperationBase.setEnvironment(Environment.SANDBOX);

        MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
        merchantAuthenticationType.setName(apiLoginId);
        merchantAuthenticationType.setTransactionKey(transactionKey);
        ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);
        
        // Create the payment transaction request
        TransactionRequestType txnRequest = new TransactionRequestType();
        txnRequest.setTransactionType(TransactionTypeEnum.AUTH_CAPTURE_TRANSACTION.value());
        txnRequest.setAmount(BigDecimal.valueOf(amount).setScale(2, RoundingMode.CEILING));

        SettingType setting1 = new SettingType();
        setting1.setSettingName("hostedPaymentButtonOptions");
        setting1.setSettingValue("{\"text\": \"Pay\"}");
        
        SettingType setting2 = new SettingType();
        setting2.setSettingName("hostedPaymentOrderOptions");
        setting2.setSettingValue("{\"show\": false}");

        ArrayOfSetting alist = new ArrayOfSetting();
        alist.getSetting().add(setting1);
        alist.getSetting().add(setting2);

        GetHostedPaymentPageRequest apiRequest = new GetHostedPaymentPageRequest();
        apiRequest.setTransactionRequest(txnRequest);
        apiRequest.setHostedPaymentSettings(alist);

        GetHostedPaymentPageController controller = new GetHostedPaymentPageController(apiRequest);
        controller.execute();
       
        GetHostedPaymentPageResponse response;
		response = controller.getApiResponse();

		if (response!=null) {

             if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

 				LOGGER.info(response.getMessages().getMessage().get(0).getCode());
                LOGGER.info(response.getMessages().getMessage().get(0).getText());

                LOGGER.info(response.getToken());
            }
            else
            {
                LOGGER.info("Failed to get hosted payment page:  " + response.getMessages().getResultCode());
            }
        }
		return response;
    }
}
