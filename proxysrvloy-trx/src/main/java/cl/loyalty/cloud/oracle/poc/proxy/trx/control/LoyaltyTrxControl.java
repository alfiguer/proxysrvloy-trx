package cl.loyalty.cloud.oracle.poc.proxy.trx.control;

import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.loyalty.cloud.oracle.poc.proxy.trx.model.LoyTrx;

public class LoyaltyTrxControl {

	private static LoyaltyTrxControl instance = null;

	public LoyaltyTrxControl() {
		super();
	}

	public static LoyaltyTrxControl getInstance() {

		if (!Optional.ofNullable(LoyaltyTrxControl.instance).isPresent()) {
			LoyaltyTrxControl.instance = new LoyaltyTrxControl();
		}
		return LoyaltyTrxControl.instance;
	}

	public Integer getAccrualTrx(String loyTrxId) {
		Response response;
		Client client;
		String username;
		String password;
		String usernameAndPassword;
		String authorizationHeaderName = "Authorization";
		String authorizationHeaderValue;
		String restResource = "https://ucf6-fap1454-fa-ext.oracledemos.com/";
		String restContentType = "application/vnd.oracle.adf.resourceitem+json";
		String responseEntity;
		StringBuilder resourcePath;
		ObjectMapper mapper;
		LoyTrx loyTrx;
		Integer amount;
		try {
			username = "denis.figueroa";
			password = "Eqj77775";
			usernameAndPassword = username + ":" + password;
			authorizationHeaderValue = "Basic "
					+ java.util.Base64.getEncoder().encodeToString(usernameAndPassword.getBytes());
			resourcePath = new StringBuilder().append("crmRestApi/resources/latest/loyaltyTransactions/");
			resourcePath.append(loyTrxId).append("?fields=AmountValue");

			client = ClientBuilder.newClient();
			response = client.target(restResource).path(resourcePath.toString()).request(restContentType)
					.header(authorizationHeaderName, authorizationHeaderValue).get();

			//System.out.println("response.getStatus(): " + response.getStatus());
			responseEntity = (response.readEntity(String.class));
			//System.out.println("response entity: " + responseEntity);

			if (response.getStatus() == 200) {
				mapper = new ObjectMapper();
				loyTrx = mapper.readValue(responseEntity, LoyTrx.class);
				amount = loyTrx.getAmountValue();
			} else {
				amount = -1;
			}

		} catch (Exception e) {
			e.printStackTrace();
			amount = -1;
		}
		return amount;
	}

	public static final void main(String[] args) {
		System.out.println(LoyaltyTrxControl.getInstance().getAccrualTrx("300000166913287"));
	}
}
