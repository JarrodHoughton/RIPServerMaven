/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOs;

import Models.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author ruthe
 */
public interface ViewDao_Interface {
    
    public Boolean addView(View view);
    public List<Integer> get10MostViewed(LocalDateTime startDate, LocalDateTime endDate);
    
}
