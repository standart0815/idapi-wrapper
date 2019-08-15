/*
 * Copyright (c) 2014 Actuate Corporation
 */

package com.actuate.aces.idapi;

import com.actuate.aces.idapi.control.ActuateException;
import com.actuate.schemas.*;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * Created by rmurphy on 7/25/2014.
 */
public class UserGroupUnassigner extends BaseController {
	public UserGroupUnassigner(BaseController controller) {
		super(controller);
	}

	public UserGroupUnassigner(String host, String authenticationId) throws MalformedURLException, ServiceException {
		super(host, authenticationId);
	}

	public UserGroupUnassigner(String host, String username, String password, String volume) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume);
	}

	public UserGroupUnassigner(String host, String username, String password, String volume, byte[] extendedCredentials) throws ServiceException, ActuateException, MalformedURLException {
		super(host, username, password, volume, extendedCredentials);
	}

	public boolean unassignGroup(String user, String[] groups) throws RemoteException {
		ArrayOfString groupArray = new ArrayOfString(groups);
		UpdateUserOperation updateUserOperation = new UpdateUserOperation();
		updateUserOperation.setDropUserGroupsByName(groupArray);
		UserCondition userCondition = new UserCondition();
		userCondition.setField(UserField.Name);
		userCondition.setMatch(user.replace("-","\\-"));
		UserSearch userSearch = new UserSearch();
		userSearch.setCondition(userCondition);

		UpdateUserOperationGroup updateUserOperationGroup = new UpdateUserOperationGroup();
		updateUserOperationGroup.setUpdateUserOperation(new UpdateUserOperation[]{updateUserOperation});

		UpdateUser updateUser = new UpdateUser();
		updateUser.setSearch(userSearch);
		updateUser.setUpdateUserOperationGroup(updateUserOperationGroup);

		AdminOperation[] adminOperations = new AdminOperation[permissions == null ? 1 : 2];
		adminOperations[0] = new AdminOperation();
		adminOperations[0].setUpdateUser(updateUser);
		Administrate administrate = new Administrate();
		administrate.setAdminOperation(adminOperations);
		//try {
			acxControl.proxy.administrate(administrate);
		//} catch (RemoteException e) {
		//	e.printStackTrace();
		//	return false;
		//}

		return true;
	}
}
