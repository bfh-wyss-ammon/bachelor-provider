package data;


public class DbGroup {
	private Integer providerGroupId;
	private Integer groupId;
	private DbPublicKey publicKey;
	
	
	public Integer getProviderGroupId() {
		return providerGroupId;
	}
	public void setProviderGroupId(Integer providerGroupId) {
		this.providerGroupId = providerGroupId;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public DbPublicKey getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(DbPublicKey publicKey) {
		this.publicKey = publicKey;
	}
	
}
