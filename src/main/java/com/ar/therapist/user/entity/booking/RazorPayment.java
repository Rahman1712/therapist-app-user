package com.ar.therapist.user.entity.booking;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RazorPayment {

	@Id
	private String razorpay_payment_id;
	private String razorpay_order_id;
	private String razorpay_signature;
	private String bookingId;
	private Double amount;
}

/* 
 {
 	razorpay_payment_id: 'pay_MHvxr7K0SeiX8s', 
 	razorpay_order_id: 'order_MHvxRVeUTCEVAU', 
 	razorpay_signature: 'a6ac2256dab7aac7a740488adbcb79527f9e2c8f9608c37ae98289a3751d8802'
  } 
 */
