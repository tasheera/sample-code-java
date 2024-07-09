package net.authorize.sample.customerprofiles;

import net.authorize.Environment;
import net.authorize.api.contract.v1.*;
import net.authorize.api.controller.CreateCustomerProfileController;
import net.authorize.api.controller.base.ApiOperationBase;

import java.util.logging.Logger;

public class CreateCustomerProfile {

    private static final Logger LOGGER = Logger.getLogger(CreateCustomerProfile.class.getName());

    private CreateCustomerProfile(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static ANetApiResponse run(String apiLoginId, String transactionKey, String eMail) {

        // Set the request to operate in either the sandbox or production environment
        ApiOperationBase.setEnvironment(Environment.SANDBOX);
        
        // Create object with merchant authentication details
        MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
        merchantAuthenticationType.setName(apiLoginId);
        merchantAuthenticationType.setTransactionKey(transactionKey);

        // Populate the payment data
        CreditCardType creditCard = new CreditCardType();
        creditCard.setCardNumber("4111111111111111");
        creditCard.setExpirationDate("1235");
        PaymentType paymentType = new PaymentType();
        paymentType.setCreditCard(creditCard);

        // Set payment profile data
        CustomerPaymentProfileType customerPaymentProfileType = new CustomerPaymentProfileType();
        customerPaymentProfileType.setCustomerType(CustomerTypeEnum.INDIVIDUAL);
        customerPaymentProfileType.setPayment(paymentType);

        // Set customer profile data
        CustomerProfileType customerProfileType = new CustomerProfileType();
        customerProfileType.setMerchantCustomerId("M_" + eMail);
        customerProfileType.setDescription("Profile description for " + eMail);
        customerProfileType.setEmail(eMail);
        customerProfileType.getPaymentProfiles().add(customerPaymentProfileType);

        // Create the API request and set the parameters for this specific request
        CreateCustomerProfileRequest apiRequest = new CreateCustomerProfileRequest();
        apiRequest.setMerchantAuthentication(merchantAuthenticationType);
        apiRequest.setProfile(customerProfileType);
        apiRequest.setValidationMode(ValidationModeEnum.TEST_MODE);

        // Call the controller
        CreateCustomerProfileController controller = new CreateCustomerProfileController(apiRequest);
        controller.execute();

        // Get the response
        CreateCustomerProfileResponse response;
        response = controller.getApiResponse();
        
        // Parse the response to determine results
        if (response!=null) {
            // If API Response is OK, go ahead and check the transaction response
            if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {
                LOGGER.info(response.getCustomerProfileId());
                if (!response.getCustomerPaymentProfileIdList().getNumericString().isEmpty()) {
                    LOGGER.info(response.getCustomerPaymentProfileIdList().getNumericString().get(0));
                }
                if (!response.getCustomerShippingAddressIdList().getNumericString().isEmpty()) {
                    LOGGER.info(response.getCustomerShippingAddressIdList().getNumericString().get(0));
                }
                if (!response.getValidationDirectResponseList().getString().isEmpty()) {
                    LOGGER.info(response.getValidationDirectResponseList().getString().get(0));
                }
            }
            else
            {
                LOGGER.info("Failed to create customer profile:  " + response.getMessages().getResultCode());
            }
        } else {
            // Display the error code and message when response is null 
            ANetApiResponse errorResponse = controller.getErrorResponse();
            LOGGER.info("Failed to get response");
            if (!errorResponse.getMessages().getMessage().isEmpty()) {
                LOGGER.info("Error: "+errorResponse.getMessages().getMessage().get(0).getCode()+" \n"+ errorResponse.getMessages().getMessage().get(0).getText());
            }
        }
        return response;

    }
}