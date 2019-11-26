package com.diviso.graeshoppe.client.SMS;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "SMSResource", url= "${smsgateway.uk-url}")
public interface SMSResourceApiClientUK extends SMSResourceApiUK{

}
