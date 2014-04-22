package com.yolosec.data;

import com.ptsesd.groepb.shared.MessagingComm;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface MessageDAO {
    List<MessagingComm> findAll();
    
    void resetMessages();
    
}
