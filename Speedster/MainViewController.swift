//
//  MainViewController.swift
//  Speedster
//
//  Created by Erphun Ranjbar on 10/14/16.
//  Copyright © 2016 Hasnain Bilgrami. All rights reserved.
//

import UIKit
import MapKit
import AVFoundation
import CoreLocation
import CoreData
import UserNotifications

extension Notification.Name {
    static let speedwarning = Notification.Name("speedWarning")
}

class MainViewController: UIViewController, MKMapViewDelegate, CLLocationManagerDelegate, UNUserNotificationCenterDelegate {
    
    @IBOutlet weak var roadSpeedLimitLabel: UILabel!
    @IBOutlet weak var mapView: MKMapView!
    @IBOutlet weak var currentAddressLabel: UILabel!
    @IBOutlet weak var currentUserSpeedLabel: UILabel!
    @IBOutlet weak var topBarColorLabel: UILabel!
    var locationManager =  CLLocationManager()
    var currentLocation:CLLocation!
    
    //GLOBAL VARIABLES SO THAT WE CAN COUNT DURATION OF INFRACTTION
    var infractSpeedRN:String = ""
    var infractMaxSpeed:String = ""
    var infractSTLat:String = ""
    var infractSTLong:String = ""
    var infractDate = NSDate()
//    var infractTime:DispatchTime = DispatchTime.now()
    var infractTime:Int64 = 0
    var hasBeenRed = false
    var soundCounter = -1
    
    var speedingTimer = Timer()
    var infractionStopwatch = 0
    
    //currently hard coded need a fix for api
    var maxSpeed:Int =  7
    var speedInMph:Float = 0.0
    var listItems = [NSManagedObject]()
    //history of CLLocation
    var myLocations: [CLLocation] = []

    let customRed = UIColor(red: 188/255, green: 11/255, blue: 11/255, alpha: 1)
    let customYellow = UIColor(red: 239/255, green: 219/255, blue: 59/2355, alpha: 1)
    let customGreen = UIColor(red: 18/255, green: 155/255, blue: 13/2355, alpha: 1)
    
    //sets up timer as an object of the Timer class
    var timer = Timer()
    
    //set up reference of sound `police_noise.mp3` and sets up audioPlayer as a class of AVAudioPlayer
    var notifSound = URL(fileURLWithPath: Bundle.main.path(forResource: "notification_sound", ofType: "mp3")!)
    var audioPlayer = AVAudioPlayer()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mapView.delegate = self
        locationManager.delegate = self
        self.currentUserSpeedLabel.textColor = customGreen
        navigationController?.navigationBar.barTintColor = UIColor(red: 190/255, green: 230/255, blue: 170/255, alpha: 1)
        
        UNUserNotificationCenter.current().delegate = self
        checkAuthorization()
        checkSpeedLimit()
        
        // Do any additional setup after loading the view.
    }
    
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func checkSpeedLimit(){
        roadSpeedLimitLabel?.text = String(maxSpeed) + " MPH"
        
        //Do comparison every 3 seconds
        timer = Timer.scheduledTimer(timeInterval: 3.0, target: self, selector: #selector(doSpeedTest), userInfo: self, repeats: true)

    }
    
    //plays sound
    func playNotif(){
        do{
            audioPlayer = try AVAudioPlayer(contentsOf: notifSound)
            audioPlayer.prepareToPlay()
            audioPlayer.play()
            audioPlayer.numberOfLoops = 0
        }catch _ as NSError {
            print("sound not found")
        }
    }
    
    func getCurrentMillis()->Int64 {
        return Int64(Date().timeIntervalSince1970 * 1000)
    }
    
    
    //Comparison of the speeds
    func doSpeedTest(timer: Timer) {
       
        let currentSpeed = Int(speedInMph)
    
        if ((maxSpeed+10) <= currentSpeed){
            
            //waits 3 seconds before playing, playing sound every 45 seconds that user is speeding =>
            //checking speed every 3 seconds, increment counter by 1 and modulo 15
    
            //controls audio player
            soundCounter += 1
            
            //plays sound
            if(soundCounter % 15 == 1){
                playNotif()
            }
        
            if(currentUserSpeedLabel.textColor != customRed) {
                //Start of infraction
                let date = NSDate()
                let calendar = NSCalendar.current
                let components = calendar.dateComponents([.year, .month, .day, .hour, .minute], from: date as Date)
                
                let annotation = MKPointAnnotation()
                let speedRN = round((locationManager.location?.speed)! * 2.23694)
                annotation.coordinate = mapView.userLocation.coordinate
                annotation.title = "\(speedRN) MPH in a \(maxSpeed) MPH zone"
                annotation.subtitle = "\(components.month!)-\(components.day!)-\(components.year!) at \(components.hour!):\(components.minute!)"
                
                mapView.addAnnotation(annotation)
                let numLat = (Double(mapView.userLocation.coordinate.latitude))
                let numLong = (Double(mapView.userLocation.coordinate.longitude))
                
                //SETTING STUFF UP FOR GLOBAL ENTRY
                infractSTLat = String(numLat)
                infractSTLong = String(numLong)
                infractTime = self.getCurrentMillis()
                infractSpeedRN = String(speedRN)
                infractMaxSpeed = String(maxSpeed)
                infractDate = NSDate()
                hasBeenRed = true
                
                //User notifications contents
                let content =  UNMutableNotificationContent()
                content.title = "You just went over the speed limit!"
                content.body = "Your speed is \(currentSpeed) and the speed limit is \(maxSpeed)"
                
                content.sound = UNNotificationSound.default()
                
                let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 5, repeats: false)
                
                let requestIdentifier = "Speedster"
                let request = UNNotificationRequest(identifier: requestIdentifier, content: content, trigger: trigger)
                UNUserNotificationCenter.current().add(request, withCompletionHandler: nil)
                
            }

            navigationController?.navigationBar.barTintColor = UIColor(red: 225/255, green: 125/255, blue: 120/255, alpha: 1)
            currentUserSpeedLabel.textColor = customRed
            
        }
        else if (maxSpeed+5) <= currentSpeed {
            self.currentUserSpeedLabel.textColor = customYellow
            navigationController?.navigationBar.barTintColor = UIColor(red: 255/255, green: 248/255, blue: 129/255, alpha: 1)
        }
        else{
            soundCounter = 0
            
            //end of infractions
            if(hasBeenRed){
                let nanoTime =  self.getCurrentMillis() - infractTime
                //let timeInSpeed = (Double(nanoTime)/10000000)
                let timeInSpeed = (Double(nanoTime)/1000)
                
                seedInfraction(currentSpeed: infractSpeedRN, speedLimit: infractMaxSpeed, latitude: infractSTLat, longitude: infractSTLong, date: infractDate, timeSpent: timeInSpeed)
                
                hasBeenRed = false
            }
          
            self.currentUserSpeedLabel.textColor = customGreen
            navigationController?.navigationBar.barTintColor = UIColor(red: 190/255, green: 230/255, blue: 170/255, alpha: 1)
        }
        //stops timer so sound is on loop
    }

    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        completionHandler()
    }
    
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        completionHandler( [.alert, .sound, .badge])
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        let locValue:CLLocationCoordinate2D = manager.location!.coordinate
        let currentLocation2 = CLLocation(latitude: locValue.latitude, longitude: locValue.longitude)
        
        //to deal with setting user speed
        let currentSpeed = manager.location!.speed
        
        //.speed is in meters per second, have to convert by * 3.6 for km/h or * 2.23694 for mph
        speedInMph = Float((currentSpeed) * 2.23694)
        
        var speedInMphInt = (round(speedInMph))
        if speedInMphInt <= 0{
            speedInMphInt = 0
        }
        currentUserSpeedLabel.text! = String(format: "%g MPH", speedInMphInt)
    
        let spanX = 0.01
        let spanY = 0.01
        let newRegion = MKCoordinateRegion(center: (locationManager.location?.coordinate)!, span: MKCoordinateSpanMake(spanX, spanY))
        mapView.setRegion(newRegion, animated: true)
        
        myLocations.append(locations[0] as CLLocation)
        
        if (myLocations.count > 1){
            let sourceIndex = myLocations.count - 1
            let destinationIndex = myLocations.count - 2
            
            let c1 = myLocations[sourceIndex].coordinate
            let c2 = myLocations[destinationIndex].coordinate
            var a = [c1, c2]
            let polyline = MKPolyline(coordinates: &a, count: a.count)
            mapView.add(polyline)
        }
        
        reverseGeocodeLocation(clLocation: currentLocation2)
    }
    
    //Draws the line on the map
    func mapView(_ mapView: MKMapView, rendererFor overlay: MKOverlay) -> MKOverlayRenderer {
        assert(overlay is MKPolyline, "overlay must be polyline")
        
        let polylineRenderer = MKPolylineRenderer(overlay: overlay)
        
        if Float((locationManager.location?.speed)! * 2.23694) > (Float(maxSpeed) + 10){
            polylineRenderer.strokeColor = customRed
        }
        else if Float((locationManager.location?.speed)! * 2.23694) > (Float(maxSpeed) + 5){
            polylineRenderer.strokeColor = customYellow
        }else{
            polylineRenderer.strokeColor = customGreen
        }
        polylineRenderer.lineWidth = 2
        return polylineRenderer
    }
    
    
    func checkAuthorization(){
        locationManager.requestAlwaysAuthorization()
        mapView.showsUserLocation = true
        if CLLocationManager.authorizationStatus() == .authorizedWhenInUse{
            mapView.showsUserLocation = true
            
        }
        else{
            locationManager.requestWhenInUseAuthorization()
        }
        
        if CLLocationManager.locationServicesEnabled() {
            locationManager.delegate = self
            locationManager.desiredAccuracy = kCLLocationAccuracyBestForNavigation
            locationManager.startUpdatingLocation()
        }
    }
    
    //ReverseGeocode
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
                
                    self.currentAddressLabel.text! = address
                }else{
                    self.currentAddressLabel.text! = "Unknown"

                }
            }
        }
    }
    
    //UPDATING TO TAKE INFRACTIONTIME
    func seedInfraction(currentSpeed:String, speedLimit:String, latitude:String, longitude:String, date:NSDate, timeSpent: Double) {
        
        // create an instance of our managedObjectContext
        let moc = DataController().managedObjectContext
        
        // we set up our entity by selecting the entity and context that we're targeting
        let entity = NSEntityDescription.insertNewObject(forEntityName: "SpeedInfraction", into: moc) as! SpeedInfraction
        
        // add our data
        entity.setValue(currentSpeed, forKey: "currentSpeed")
        entity.setValue(speedLimit, forKey: "speeLimit")
        entity.setValue(latitude, forKey: "latitdue")
        entity.setValue(longitude, forKey: "longitude")
        entity.setValue(date, forKey:"date")
        entity.setValue(timeSpent, forKey: "infractionTime")
        
        // we save our entity
        do {
            try moc.save()
        } catch {
            fatalError("Failure to save context: \(error)")
        }
    }
    
    
    //Support for background fetch
    func application(application: UIApplication, performFetchWithCompletionHandler completionHandler: (UIBackgroundFetchResult) -> Void) {
        
        completionHandler(UIBackgroundFetchResult.newData)
        checkSpeedLimit()
    }
    
}
