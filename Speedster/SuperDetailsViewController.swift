//
//  SuperDetailsViewController.swift
//  Speedster
//
//  Created by Ahmed Sleem on 11/14/16.
//  Copyright © 2016 Hasnain Bilgrami. All rights reserved.
//

import UIKit
import MapKit
import Foundation

class SuperDetailsViewController: UIViewController {
    
    var headline: String!
    var subHeadLine: String!
    var locationInfo = CLLocation()
    
    @IBOutlet weak var dateLabel: UILabel!
    @IBOutlet weak var locationStreetLabel: UILabel!
    @IBOutlet weak var headlineLabel: UILabel!
    @IBOutlet weak var infractTimeLabel: UILabel!
    @IBOutlet weak var clockLabel: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //parsing the data to dispaly
        let fullSubHead: String = subHeadLine
        let fullSubHeadArr = fullSubHead.components(separatedBy: "-")
        let subHead: String = fullSubHeadArr[0]
        let timeDuration: String = fullSubHeadArr[1]
        let subHead2 = subHead.components(separatedBy: "at")
        let dateInfo = subHead2[0]
        let clockInfo = subHead2[1]
        
        headlineLabel.text = headline
        dateLabel.text = dateInfo
        infractTimeLabel.text = timeDuration
        clockLabel.text = "at \(clockInfo)"
        reverseGeocodeLocation(clLocation: locationInfo)
        // Do any additional setup after loading the view.
    }
    
    func reverseGeocodeLocation(clLocation: CLLocation){
        CLGeocoder().reverseGeocodeLocation(clLocation) { (placemarks, error) in
            if error != nil {
                return
            }
            guard let places = placemarks else{
                return
            }
            if places.count > 0{
                let myPlaceMark = places[0]
                
                if myPlaceMark.thoroughfare != nil{
                    let address = myPlaceMark.thoroughfare!
                    
                    self.locationStreetLabel.text! = address
                }else{
                    self.locationStreetLabel.text! = "Unknown"
                    
                }
            }
        }
    }


    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
