package net.authorize.sample.customerprofiles;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.authorize.Environment;
import net.authorize.api.contract.v1.ANetApiResponse;
import net.authorize.api.contract.v1.CreateCustomerProfileFromTransactionRequest;
import net.authorize.api.contract.v1.CreateCustomerProfileResponse;
import net.authorize.api.contract.v1.CreateTransactionRequest;
import net.authorize.api.contract.v1.CreateTransactionResponse;
import net.authorize.api.contract.v1.CreditCardType;
import net.authorize.api.contract.v1.CustomerDataType;
import net.authorize.api.contract.v1.CustomerProfileBaseType;
import net.authorize.api.contract.v1.MerchantAuthenticationType;
import net.authorize.api.contract.v1.PaymentType;
import net.authorize.api.contract.v1.TransactionRequestType;
import net.authorize.api.controller.CreateCustomerProfileFromTransactionController;
import net.authorize.api.controller.CreateTransactionController;
import net.authorize.api.controller.base.ApiOperationBase;
import java.util.logging.Logger;


public class CreateCustomerProfileFromTransaction {

	private static final Logger LOGGER = Logger.getLogger(CreateCustomerProfileFromTransaction.class.getName());

	private CreateCustomerProfileFromTransaction(){
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

	public static ANetApiResponse run(String apiLoginId, String transactionKey, Double amount, String email) {

		ApiOperationBase.setEnvironment(Environment.SANDBOX);

        MerchantAuthenticationType merchantAuthenticationType  = new MerchantAuthenticationType() ;
        merchantAuthenticationType.setName(apiLoginId);
        merchantAuthenticationType.setTransactionKey(transactionKey);
        ApiOperationBase.setMerchantAuthentication(merchantAuthenticationType);
		
		CreditCardType creditCard = new CreditCardType();
		creditCard.setCardNumber("4111111111111111");
	    creditCard.setExpirationDate("0635");
				
		PaymentType paymentType = new PaymentType();
		paymentType.setCreditCard(creditCard);
				
		TransactionRequestType requestInternal = new TransactionRequestType();
		requestInternal.setTransactionType("authOnlyTransaction");
		requestInternal.setPayment(paymentType);
		requestInternal.setAmount(BigDecimal.valueOf(amount).setScale(2, RoundingMode.CEILING));
		
		CustomerDataType customer = new CustomerDataType();
		customer.setEmail(email);
		requestInternal.setCustomer(customer);
				
		CreateTransactionRequest request = new CreateTransactionRequest();
		request.setTransactionRequest(requestInternal);
				
		CreateTransactionController controller = new CreateTransactionController(request);
		controller.execute();
				
		CreateTransactionResponse response = controller.getApiResponse();

		CustomerProfileBaseType customerProfile = new CustomerProfileBaseType();
		customerProfile.setMerchantCustomerId("123213");
		customerProfile.setEmail("johnsnow@castleblack.com");
		customerProfile.setDescription("This is a sample customer profile");		
		
		CreateCustomerProfileFromTransactionRequest transactionRequest = new CreateCustomerProfileFromTransactionRequest();
		transactionRequest.setTransId(response.getTransactionResponse().getTransId());
		// You can either specify the customer information in form of customerProfileBaseType object
		transactionRequest.setCustomer(customerProfile);
		//  OR   
		// You can just provide the customer Profile ID

		CreateCustomerProfileFromTransactionController createProfileController = new CreateCustomerProfileFromTransactionController(transactionRequest);
		createProfileController.execute();
		CreateCustomerProfileResponse customerResponse = createProfileController.getApiResponse();

		if (customerResponse != null) {
			LOGGER.info(transactionRequest.getTransId());
		}
		return customerResponse;
	}
}
