package gka.funflowers_integration.purchase;

import com.flexionmobile.codingchallenge.integration.Purchase;

public class IntegrationPurchase implements Purchase {

	private boolean consumed;
	private String id;
	private String ItemId;

	public IntegrationPurchase() {
	}

	public IntegrationPurchase(boolean consumed, String id, String itemId) {

	}

	public void setConsumed(boolean consumed) {
		this.consumed = consumed;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setItemId(String itemId) {
		ItemId = itemId;
	}

	public boolean getConsumed() {
		return consumed;
	}

	public String getId() {
		return id;
	}

	public String getItemId() {
		return ItemId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ItemId == null) ? 0 : ItemId.hashCode());
		result = prime * result + (consumed ? 1231 : 1237);
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntegrationPurchase other = (IntegrationPurchase) obj;
		if (ItemId == null) {
			if (other.ItemId != null)
				return false;
		} else if (!ItemId.equals(other.ItemId))
			return false;
		if (consumed != other.consumed)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IntegrationPurchase [consumed=" + consumed + ", id=" + id
				+ ", ItemId=" + ItemId + "]";
	}

}
