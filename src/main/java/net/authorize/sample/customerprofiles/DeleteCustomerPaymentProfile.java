package net.authorize.sample.customerprofiles;

import net.authorize.Environment;
import net.authorize.api.contract.v1.*;

import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.controller.base.ApiOperationBase;
import net.authorize.api.controller.DeleteCustomerPaymentProfileController;
import java.util.logging.Logger;


public class DeleteCustomerPaymentProfile {

    private static final Logger LOGGER = Logger.getLogger(DeleteCustomerPaymentProfile.class.getName());

    private DeleteCustomerPaymentProfile(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
	
	public static ANetApiResponse run(String apiLoginId, String transactionKey, String customerProfileId,
			String customerPaymentProfileId) {

        ApiOperationBase.setEnvironment(Environment.SANDBOX);

        MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
        merchantAuthenticationType.setName(apiLoginId);
        merchantAuthenticationType.setTransactionKey(transactionKey);
        ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

        DeleteCustomerPaymentProfileRequest apiRequest = new DeleteCustomerPaymentProfileRequest();
        apiRequest.setCustomerProfileId(customerProfileId);
        apiRequest.setCustomerPaymentProfileId(customerPaymentProfileId);

        DeleteCustomerPaymentProfileController controller = new DeleteCustomerPaymentProfileController(apiRequest);
        controller.execute();
       
		DeleteCustomerPaymentProfileResponse response;
		response = controller.getApiResponse();

		if (response!=null) {

             if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

 				LOGGER.info(response.getMessages().getMessage().get(0).getCode());
                LOGGER.info(response.getMessages().getMessage().get(0).getText());
            }
            else
            {
                LOGGER.info("Failed to delete customer payment profile:  " + response.getMessages().getResultCode());
            }
        }
		return response;
    }
}