package net.authorize.sample.acceptsuite;

import net.authorize.Environment;
import net.authorize.api.contract.v1.*;

import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.controller.base.ApiOperationBase;
import net.authorize.api.controller.GetHostedProfilePageController;

import java.util.logging.Logger;

public class GetAcceptCustomerProfilePage {

    private static final Logger LOGGER = Logger.getLogger(GetAcceptCustomerProfilePage.class.getName());

    private GetAcceptCustomerProfilePage(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
	
	public static ANetApiResponse run(String apiLoginId, String transactionKey, String customerProfileId) {

        ApiOperationBase.setEnvironment(Environment.SANDBOX);

        MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
        merchantAuthenticationType.setName(apiLoginId);
        merchantAuthenticationType.setTransactionKey(transactionKey);
        ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

        SettingType setting = new SettingType();
        setting.setSettingName("hostedProfileReturnUrl");
        setting.setSettingValue("https://returnurl.com/return/");

        ArrayOfSetting alist = new ArrayOfSetting();
        alist.getSetting().add(setting);

        GetHostedProfilePageRequest apiRequest = new GetHostedProfilePageRequest();
        apiRequest.setCustomerProfileId(customerProfileId);
        apiRequest.setHostedProfileSettings(alist);

        GetHostedProfilePageController controller = new GetHostedProfilePageController(apiRequest);
        controller.execute();
       
		GetHostedProfilePageResponse response;
		response = controller.getApiResponse();

		if (response!=null) {

             if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

 				LOGGER.info(response.getMessages().getMessage().get(0).getCode());
                LOGGER.info(response.getMessages().getMessage().get(0).getText());

                LOGGER.info(response.getToken());
            }
            else
            {
                LOGGER.info("Failed to get hosted profile page:  " + response.getMessages().getResultCode());
            }
        }
		return response;
    }
}
