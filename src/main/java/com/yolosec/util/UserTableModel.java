package com.yolosec.util;

import com.yolosec.domain.User;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Administrator
 */
public class UserTableModel extends AbstractTableModel {

    private final List<User> userList;
    private final String[] columnNames = {"ID", "Username", "Password", "isModerator", "isLoggedIn"};

    public UserTableModel(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return this.columnNames[columnIndex];
    }

    @Override
    public int getRowCount() {
        return this.userList.size();
    }

    @Override
    public int getColumnCount() {
        return this.columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = userList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return user.getId();
            case 1:
                return user.getUsername();
            case 2:
                return user.getPassword();
            case 3:
                return user.getMod();
            case 4:
                return user.getIsLoggedIn();
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return Boolean.class;
            case 4:
                return Boolean.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column > 0;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        User user = userList.get(rowIndex);
        switch (columnIndex) {
            case 1:
                user.setUsername((String) aValue);
                break;
            case 2:
                user.setPassword((String) aValue);
                break;
            case 3:
                user.setMod((boolean) aValue);
            case 4:
                user.setIsLoggedIn((boolean) aValue);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public void removeRow(int row) {
        this.userList.remove(row);
    }
    
    public void addRow(User user) {
        this.userList.add(user);
    }
    
    public List<User> getUserList() {
        return this.userList;
    }
}
