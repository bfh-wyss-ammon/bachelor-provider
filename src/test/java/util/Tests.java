package util;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;

import org.hibernate.boot.model.relational.Database;
import org.junit.Test;

import data.BaseSignature;
import data.DbGroup;
import data.DbPublicKey;
import data.DbSignature;
import data.DbTuple;
import data.Tuple;
import demo.DemoManagerKey;
import demo.DemoSecretKey;
import demo.DemoSignature;
import keys.ManagerKey;
import keys.SecretKey;
import requests.JoinRequest;
import responses.JoinResponse;

public class Tests {

	@Test
	public void testSignAndVerify() {

		DbPublicKey publicKey = new DbPublicKey();
		ManagerKey managerKey = new DemoManagerKey();
		DbGroup group = new DbGroup();

		try {
			Generator.generate(publicKey, managerKey);

			DatabaseHelper.Save(DbPublicKey.class, publicKey);

			group.setGroupId(0);
			group.setPublicKey(publicKey);
			DatabaseHelper.Save(DbGroup.class, group);

			// aufclient
			SecretKey memberKey = new DemoSecretKey();
			JoinHelper.init(publicKey, memberKey);

			JoinRequest joinRequest = new JoinRequest(memberKey);

			JoinResponse joinResponse = JoinHelper.join(publicKey, managerKey, joinRequest);

			memberKey.maintainResponse(joinResponse);

			Tuple tuple = new Tuple();
			tuple.setGroupId(0);
			tuple.setLatitude(new BigDecimal("9").setScale(10, RoundingMode.HALF_UP));
			tuple.setLongitude(new BigDecimal("8").setScale(10, RoundingMode.HALF_UP));
			tuple.setCreated(new Date());

			BaseSignature signature = new BaseSignature();
			SignHelper.sign(memberKey, publicKey, HashHelper.getHash(tuple), signature);

			tuple.setSignature(signature);

			// -> tuple wird serialisert an Server gesendet...

			// --< server erhält tuple

			DbGroup groupServer = DatabaseHelper.Get(DbGroup.class, " groupId =" + tuple.getGroupId());

			DbTuple dpTuple = new DbTuple(tuple, group);

			assertTrue(VerifyHelper.verify(group.getPublicKey(), tuple.getSignature(), HashHelper.getHash(tuple)));

			dpTuple.setReceived(new Date());

			DatabaseHelper.Save(DbTuple.class, dpTuple);

			DbTuple storedDbTuple = DatabaseHelper.Get(DbTuple.class, dpTuple.getTupleId());

			Boolean res = VerifyHelper.verify(groupServer.getPublicKey(), storedDbTuple.getSignature(),
					HashHelper.getHash(storedDbTuple));

			
			DatabaseHelper.Delete(storedDbTuple);
			
			assertTrue(res);

		} catch (Exception ex) {

		} finally {
			DatabaseHelper.Delete(group);

		}
	}

}
