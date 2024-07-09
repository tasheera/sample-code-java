package net.authorize.sample.customerprofiles;

import net.authorize.Environment;
import net.authorize.api.contract.v1.*;

import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.controller.CreateCustomerPaymentProfileController;
import net.authorize.api.controller.base.ApiOperationBase;

import java.util.logging.Logger;

//author @krgupta
public class CreateCustomerPaymentProfile {
	private static final Logger LOGGER = Logger.getLogger(CreateCustomerPaymentProfile.class.getName());

	private CreateCustomerPaymentProfile(){
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static ANetApiResponse run(String apiLoginId, String transactionKey, String customerProfileId) {

        ApiOperationBase.setEnvironment(Environment.SANDBOX);

        MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
        merchantAuthenticationType.setName(apiLoginId);
        merchantAuthenticationType.setTransactionKey(transactionKey);
        ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);
		
		CreateCustomerPaymentProfileRequest apiRequest = new CreateCustomerPaymentProfileRequest();
		apiRequest.setMerchantAuthentication(merchantAuthenticationType);
		apiRequest.setCustomerProfileId(customerProfileId);	

		//customer address
		CustomerAddressType customerAddress = new CustomerAddressType();
		customerAddress.setFirstName("test");
		customerAddress.setLastName("scenario");
		customerAddress.setAddress("123 Main Street");
		customerAddress.setCity("Bellevue");
		customerAddress.setState("WA");
		customerAddress.setZip("98004");
		customerAddress.setCountry("USA");
		customerAddress.setPhoneNumber("000-000-0000");

		//credit card details
		CreditCardType creditCard = new CreditCardType();
		creditCard.setCardNumber("4111111111111111");
		creditCard.setExpirationDate("2035-12");
		creditCard.setCardCode("122");

		CustomerPaymentProfileType profile = new CustomerPaymentProfileType();
		profile.setBillTo(customerAddress);

		PaymentType payment = new PaymentType();
		payment.setCreditCard(creditCard);
		profile.setPayment(payment);

		apiRequest.setPaymentProfile(profile);
		
		CreateCustomerPaymentProfileController controller = new CreateCustomerPaymentProfileController(apiRequest);
		controller.execute();
       
		CreateCustomerPaymentProfileResponse response;
		response = controller.getApiResponse();
		if (response!=null) {
             if (response.getMessages().getResultCode() == MessageTypeEnum.OK) {
            	
                LOGGER.info(response.getCustomerPaymentProfileId());
				LOGGER.info(response.getMessages().getMessage().get(0).getCode());
				LOGGER.info(response.getMessages().getMessage().get(0).getText());
                if (response.getValidationDirectResponse() != null)
                	LOGGER.info(response.getValidationDirectResponse());
            }
            else
            {
                LOGGER.info("Failed to create customer payment profile:  " + response.getMessages().getResultCode());
            }
        }

		return response;
	}	
}
