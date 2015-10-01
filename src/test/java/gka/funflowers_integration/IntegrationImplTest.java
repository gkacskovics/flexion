package gka.funflowers_integration;

import static org.junit.Assert.fail;
import gka.funflowers_integration.developer.Developer;

import java.util.List;

import org.junit.Test;

import com.flexionmobile.codingchallenge.integration.Integration;
import com.flexionmobile.codingchallenge.integration.Purchase;

public class IntegrationImplTest {

	private static final String API = "http://dev2.flexionmobile.com/javachallenge/rest";
	private static final Developer dev = new Developer("gkacskovics");

	Integration integration = new IntegrationImpl(API, dev);

	@Test
	public void provided_tests() {
		try {
			IntegrationTestRunner runner = new IntegrationTestRunner();
			runner.runTests(integration);

		} catch (Exception e) {
			fail("Should have no exceptions, but: " + e.getMessage());
		}
	}

	private static class IntegrationTestRunner {

		public void runTests(Integration integration) {
			if (integration == null) {
				throw new IllegalStateException("test failed: you must pass an instance of Integration");
			}
			Purchase purchase = integration.buy("item1");
			if (purchase == null) {
				throw new IllegalStateException("test failed: buy did not result in a purchase");
			}
			if (purchase.getConsumed()) {
				throw new IllegalStateException("test failed: purchase should not be marked as consumed before consumption");
			}
			try {
				integration.consume(purchase);
			} catch (Exception e) {
				throw new IllegalStateException("test failed: failed to consume purchase", e);
			}
			String alreadyConsumedPurchaseId = purchase.getId();
			if (alreadyConsumedPurchaseId == null || alreadyConsumedPurchaseId.isEmpty()) {
				throw new IllegalStateException("test failed: the returned purchase id is blank");
			}
			List<Purchase> purchases = integration.getPurchases();
			for (Purchase p : purchases) {
				String purchaseId = p.getId();
				if (purchaseId == null || purchaseId.isEmpty()) {
					throw new IllegalStateException("test failed: the returned purchase id is blank");
				}
				if (!p.getConsumed()) {
					if (alreadyConsumedPurchaseId.equals(purchaseId)) {
						throw new IllegalStateException("test failed: purchase with id '" + purchaseId + "' should already be consumed");
					}
					integration.consume(p);
				}
			}
		}
	}

}
