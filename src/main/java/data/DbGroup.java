/**
 *   Copyright 2018 Pascal Ammon, Gabriel Wyss
 * 
 * 	 Implementation eines anonymen Mobility Pricing Systems auf Basis eines Gruppensignaturschemas
 * 
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * This class stores data related to a signature group in the database of the provider.
 */

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
