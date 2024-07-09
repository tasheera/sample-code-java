package net.authorize.sample.customerprofiles;

import net.authorize.Environment;
import net.authorize.api.contract.v1.*;

import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.controller.base.ApiOperationBase;
import net.authorize.api.controller.DeleteCustomerProfileController;

import java.util.logging.Logger;

public class DeleteCustomerProfile {

    private static final Logger LOGGER = Logger.getLogger(DeleteCustomerProfile.class.getName());

    private DeleteCustomerProfile(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
	
	public static ANetApiResponse run(String apiLoginId, String transactionKey, String customerProfileId) {

        ApiOperationBase.setEnvironment(Environment.SANDBOX);

        MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
        merchantAuthenticationType.setName(apiLoginId);
        merchantAuthenticationType.setTransactionKey(transactionKey);
        ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

        DeleteCustomerProfileRequest apiRequest = new DeleteCustomerProfileRequest();
        apiRequest.setCustomerProfileId(customerProfileId);

        DeleteCustomerProfileController controller = new DeleteCustomerProfileController(apiRequest);
        controller.execute();
       
		DeleteCustomerProfileResponse response;
		response = controller.getApiResponse();

		if (response!=null) {

             if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

 				LOGGER.info(response.getMessages().getMessage().get(0).getCode());
                LOGGER.info(response.getMessages().getMessage().get(0).getText());
            }
            else
            {
                LOGGER.info("Failed to delete customer profile:  " + response.getMessages().getResultCode());
            }
        }
		return response;
    }
}