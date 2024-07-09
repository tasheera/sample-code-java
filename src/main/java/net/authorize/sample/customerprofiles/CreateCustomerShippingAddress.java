package net.authorize.sample.customerprofiles;

import net.authorize.Environment;
import net.authorize.api.contract.v1.*;
import net.authorize.api.controller.CreateCustomerShippingAddressController;
import net.authorize.api.controller.base.ApiOperationBase;
import java.util.logging.Logger;

public class CreateCustomerShippingAddress {

    private static final Logger LOGGER = Logger.getLogger(CreateCustomerShippingAddress.class.getName());


    private CreateCustomerShippingAddress(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

	public static ANetApiResponse run(String apiLoginId, String transactionKey, String customerProfileId) {

		ApiOperationBase.setEnvironment(Environment.SANDBOX);

        MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
        merchantAuthenticationType.setName(apiLoginId);
        merchantAuthenticationType.setTransactionKey(transactionKey);
        ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);

        CustomerAddressType customerAddressType = new CustomerAddressType();
        customerAddressType.setFirstName("John");
        customerAddressType.setLastName("Doe");
        customerAddressType.setAddress("123 Main St.");
        customerAddressType.setCity("Bellevue");
        customerAddressType.setState("WA");
        customerAddressType.setZip("98004");
        customerAddressType.setCountry("USA");
        customerAddressType.setPhoneNumber("000-000-0000");

        CreateCustomerShippingAddressRequest apiRequest = new CreateCustomerShippingAddressRequest();
        apiRequest.setCustomerProfileId(customerProfileId);
        apiRequest.setAddress(customerAddressType);

        CreateCustomerShippingAddressController controller = new CreateCustomerShippingAddressController(apiRequest);
        controller.execute();

        CreateCustomerShippingAddressResponse response = controller.getApiResponse();
        if (response!=null) {

             if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {

                LOGGER.info(response.getCustomerAddressId());
                LOGGER.info(response.getMessages().getMessage().get(0).getCode());
                LOGGER.info(response.getMessages().getMessage().get(0).getText());
            }
            else
            {
                LOGGER.info("Failed to create customer shipping address:  " + response.getMessages().getResultCode());
            }
        }
		return response;
 
    }
}