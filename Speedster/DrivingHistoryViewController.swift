//
//  DrivingHistoryViewController.swift
//  Speedster
//
//  Created by Ahmed Sleem on 11/2/16.
//  Copyright © 2016 Hasnain Bilgrami. All rights reserved.
//

import UIKit
import MapKit
import CoreData

extension NSDate {
    
    func isGreaterThanDate(dateToCompare: NSDate) -> Bool {
        //Declare Variables
        var isGreater = false
        
        //Compare Values
        if self.compare(dateToCompare as Date) == ComparisonResult.orderedDescending {
            isGreater = true
        }
        
        //Return Result
        return isGreater
    }
    
    func isLessThanDate(dateToCompare: NSDate) -> Bool {
        //Declare Variables
        var isLess = false
        
        //Compare Values
        if self.compare(dateToCompare as Date) == ComparisonResult.orderedAscending {
            isLess = true
        }
        
        //Return Result
        return isLess
    }
    
    func equalToDate(dateToCompare: NSDate) -> Bool {
        //Declare Variables
        var isEqualTo = false
        
        //Compare Values
        if self.compare(dateToCompare as Date) == ComparisonResult.orderedSame {
            isEqualTo = true
        }
        
        //Return Result
        return isEqualTo
    }
    
    func addDays(daysToAdd: Int) -> NSDate {
        let secondsInDays: TimeInterval = Double(daysToAdd) * 60 * 60 * 24
        let dateWithDaysAdded: NSDate = self.addingTimeInterval(secondsInDays)
        
        //Return Result
        return dateWithDaysAdded
    }
    
    func addHours(hoursToAdd: Int) -> NSDate {
        let secondsInHours: TimeInterval = Double(hoursToAdd) * 60 * 60
        let dateWithHoursAdded: NSDate = self.addingTimeInterval(secondsInHours)
        
        //Return Result
        return dateWithHoursAdded
    }
}

class MyAnnotation: NSObject,MKAnnotation {
    
    var title : String?
    var subtitle : String?
    var coordinate : CLLocationCoordinate2D
    
    init(title:String,coordinate : CLLocationCoordinate2D,subtitle:String){
        
        self.title = title;
        self.coordinate = coordinate;
        self.subtitle = subtitle;
    }
}

class DrivingHistoryViewController: UIViewController, MKMapViewDelegate, CLLocationManagerDelegate {
   
    @IBOutlet weak var historyMapView: MKMapView!
    var locationManager =  CLLocationManager()
    
    var senderTitle: String!
    var subSenderTitle: String!
    var cllocation = CLLocation()

    override func viewDidLoad() {
        super.viewDidLoad()
        historyMapView.delegate = self
        fetch()
    
        // Do any additional setup after loading the view.
    }
    
    func mapView(_ mapView: MKMapView, viewFor annotation: MKAnnotation) -> MKAnnotationView? {
        if annotation is MKUserLocation {
            return nil
        }
        
        let  pinView =  MKPinAnnotationView(annotation: annotation, reuseIdentifier: "pin")
        pinView.canShowCallout = true
        pinView.rightCalloutAccessoryView = UIButton(type: .detailDisclosure)
        return pinView
    }
    
    //adding action to the pin
    func mapView(_ mapView: MKMapView, annotationView view: MKAnnotationView, calloutAccessoryControlTapped control: UIControl) {
        if control == view.rightCalloutAccessoryView {
            cllocation = CLLocation(latitude: (view.annotation?.coordinate.latitude)!, longitude: (view.annotation?.coordinate.longitude)!)
            senderTitle = (view.annotation?.title)!
            subSenderTitle = (view.annotation?.subtitle)!
            performSegue(withIdentifier: "superdetails", sender: self)
        }
    }

   //loads all annotations from 1 day
    @IBAction func oneDayAction(_ sender: Any) {
        //removes all annotitions from mapview
        self.historyMapView.removeAnnotations(self.historyMapView.annotations)
        
        let moc = DataController().managedObjectContext
        let speedFetch = NSFetchRequest<NSFetchRequestResult>(entityName: "SpeedInfraction")

        let calendar = NSCalendar.current

        let startDate = calendar.date(byAdding: .day, value: -1, to: Date())
        let sDate = NSDate(timeInterval: 0, since: startDate!)
        
        do {
            let fetchedInfraction = try moc.fetch(speedFetch) as! [SpeedInfraction]
            
            for bob in fetchedInfraction {
                
                let doubleUserSpeed = bob.currentSpeed! as String
                let fullSubHead: String = doubleUserSpeed
                let fullSubHeadArr = fullSubHead.components(separatedBy: ".")
                let userSpeed: String = fullSubHeadArr[0]
                
                let speedLimit = bob.speeLimit! as String
                let dateOfInfrac = bob.date!
                let long = bob.longitude!
                let lat = bob.latitdue!
                let timespeed = Double(round(Double(100*bob.infractionTime))/100)
                
                let dateFormatterGet = DateFormatter()
                dateFormatterGet.dateFormat = "MMM dd, yyyy"
                let formatter = DateFormatter()
                formatter.dateFormat = "hh:mm a"
                
                let FormattedDate = dateFormatterGet.string(from: dateOfInfrac as Date)
                let FormattedTime = formatter.string(from: dateOfInfrac as Date)
                
                let coordinate = CLLocationCoordinate2DMake( CLLocationDegrees(lat )!, CLLocationDegrees(long)!)
                let annotation =  MyAnnotation(title: "\(userSpeed) MPH in a \(speedLimit) MPH zone", coordinate: coordinate, subtitle: "\(FormattedDate) at \(FormattedTime) - \(timespeed) seconds")

                if( dateOfInfrac.isGreaterThanDate(dateToCompare: sDate)){
                    historyMapView.addAnnotation(annotation)
                }
            }
            
        } catch {
            fatalError("Failed to fetch person: \(error)")
        }
    }
    
    //loads annotations from 1 week
    @IBAction func oneWeekAction(_ sender: Any) {
        self.historyMapView.removeAnnotations(self.historyMapView.annotations)
        
        let moc = DataController().managedObjectContext
        let speedFetch = NSFetchRequest<NSFetchRequestResult>(entityName: "SpeedInfraction")
        
        let calendar = NSCalendar.current
        
        let startDate = calendar.date(byAdding: .day, value: -7, to: Date())
        let sDate = NSDate(timeInterval: 0, since: startDate!)
        
        
        do {
            let fetchedInfraction = try moc.fetch(speedFetch) as! [SpeedInfraction]
            
            for bob in fetchedInfraction {
                
                let doubleUserSpeed = bob.currentSpeed! as String
                let fullSubHead: String = doubleUserSpeed
                let fullSubHeadArr = fullSubHead.components(separatedBy: ".")
                let userSpeed: String = fullSubHeadArr[0]
                
                let speedLimit = bob.speeLimit! as String
                let dateOfInfrac = bob.date!
                let long = bob.longitude!
                let lat = bob.latitdue!
                let timespeed = Double(round(Double(100*bob.infractionTime))/100)
                
                let dateFormatterGet = DateFormatter()
                dateFormatterGet.dateFormat = "MMM dd, yyyy"
                let formatter = DateFormatter()
                formatter.dateFormat = "hh:mm a"
                
                let FormattedDate = dateFormatterGet.string(from: dateOfInfrac as Date)
                let FormattedTime = formatter.string(from: dateOfInfrac as Date)
            
                let coordinate = CLLocationCoordinate2DMake( CLLocationDegrees(lat )!, CLLocationDegrees(long)!)
                     let annotation = MyAnnotation(title: "\(userSpeed) MPH in a \(speedLimit) MPH zone", coordinate: coordinate, subtitle: "\(FormattedDate) at \(FormattedTime) - \(timespeed) seconds")
                
                if( dateOfInfrac.isGreaterThanDate(dateToCompare: sDate)){
                    historyMapView.addAnnotation(annotation)
                }
            }
            
        } catch {
            fatalError("Failed to fetch person: \(error)")
        }
    }
   
    //loads annotations from 1 month
    @IBAction func oneMonthAction(_ sender: Any) {
        self.historyMapView.removeAnnotations(self.historyMapView.annotations)
        
        let moc = DataController().managedObjectContext
        let speedFetch = NSFetchRequest<NSFetchRequestResult>(entityName: "SpeedInfraction")
        
        let calendar = NSCalendar.current
        
        let startDate = calendar.date(byAdding: .month, value: -1, to: Date())
        let sDate = NSDate(timeInterval: 0, since: startDate!)
        
        
        do {
            let fetchedInfraction = try moc.fetch(speedFetch) as! [SpeedInfraction]
            
            for bob in fetchedInfraction {
            
                let doubleUserSpeed = bob.currentSpeed! as String
                let fullSubHead: String = doubleUserSpeed
                let fullSubHeadArr = fullSubHead.components(separatedBy: ".")
                let userSpeed: String = fullSubHeadArr[0]
                
                let speedLimit = bob.speeLimit! as String
                let dateOfInfrac = bob.date!
                let long = bob.longitude!
                let lat = bob.latitdue!
                let timespeed = Double(round(Double(100*bob.infractionTime))/100)
                
                let dateFormatterGet = DateFormatter()
                dateFormatterGet.dateFormat = "MMM dd, yyyy"
                let formatter = DateFormatter()
                formatter.dateFormat = "hh:mm a"
                
                let FormattedDate = dateFormatterGet.string(from: dateOfInfrac as Date)
                let FormattedTime = formatter.string(from: dateOfInfrac as Date)

                let coordinate = CLLocationCoordinate2DMake( CLLocationDegrees(lat )!, CLLocationDegrees(long)!)
                  let annotation = MyAnnotation(title: "\(userSpeed) MPH in a \(speedLimit) MPH zone", coordinate: coordinate, subtitle: "\(FormattedDate) at \(FormattedTime) - \(timespeed) seconds")
                
                if( dateOfInfrac.isGreaterThanDate(dateToCompare: sDate)){
                    historyMapView.addAnnotation(annotation)
                    
                }
            }
            
        } catch {
            fatalError("Failed to fetch person: \(error)")
        }
    }
    
    //loads annotations fom 3 months
    @IBAction func threeMonthAction(_ sender: Any) {
        self.historyMapView.removeAnnotations(self.historyMapView.annotations)
        
        let moc = DataController().managedObjectContext
        let speedFetch = NSFetchRequest<NSFetchRequestResult>(entityName: "SpeedInfraction")
        
        let calendar = NSCalendar.current
        
        let startDate = calendar.date(byAdding: .month, value: -3, to: Date())
        let sDate = NSDate(timeInterval: 0, since: startDate!)
        
        
        do {
            let fetchedInfraction = try moc.fetch(speedFetch) as! [SpeedInfraction]
            
            for bob in fetchedInfraction {
                
                let doubleUserSpeed = bob.currentSpeed! as String
                let fullSubHead: String = doubleUserSpeed
                let fullSubHeadArr = fullSubHead.components(separatedBy: ".")
                let userSpeed: String = fullSubHeadArr[0]
                
                let speedLimit = bob.speeLimit! as String
                let dateOfInfrac = bob.date!
                let long = bob.longitude!
                let lat = bob.latitdue!
                let timespeed = Double(round(Double(100*bob.infractionTime))/100)
              
                let dateFormatterGet = DateFormatter()
                dateFormatterGet.dateFormat = "MMM dd, yyyy"
                let formatter = DateFormatter()
                formatter.dateFormat = "hh:mm a"
                
                let FormattedDate = dateFormatterGet.string(from: dateOfInfrac as Date)
                let FormattedTime = formatter.string(from: dateOfInfrac as Date)
          
                let coordinate = CLLocationCoordinate2DMake( CLLocationDegrees(lat )!, CLLocationDegrees(long)!)
                let annotation = MyAnnotation(title: "\(userSpeed) MPH in a \(speedLimit) MPH zone", coordinate: coordinate, subtitle: "\(FormattedDate) at \(FormattedTime) - \(timespeed) seconds")
                
                if( dateOfInfrac.isGreaterThanDate(dateToCompare: sDate)){
                    historyMapView.addAnnotation(annotation)
                    
                }
            }
            
        } catch {
            fatalError("Failed to fetch person: \(error)")
        }
    }
    
    //loads all annotations 
    func fetch() {
        let moc = DataController().managedObjectContext
        let speedFetch = NSFetchRequest<NSFetchRequestResult>(entityName: "SpeedInfraction")
        
        do {
            let fetchedInfraction = try moc.fetch(speedFetch) as! [SpeedInfraction]
            
            for bob in fetchedInfraction {
           
                let doubleUserSpeed = bob.currentSpeed! as String
                let fullSubHead: String = doubleUserSpeed
                let fullSubHeadArr = fullSubHead.components(separatedBy: ".")
                let userSpeed: String = fullSubHeadArr[0]
            
                let speedLimit = bob.speeLimit! as String
                let dateOfInfrac = bob.date!
                let long = bob.longitude!
                let lat = bob.latitdue!
                let timespeed = Double(round(Double(100*bob.infractionTime))/100)
 
                let dateFormatterGet = DateFormatter()
                dateFormatterGet.dateFormat = "MMM dd, yyyy"
                let formatter = DateFormatter()
                formatter.dateFormat = "hh:mm a"
                
                let FormattedDate = dateFormatterGet.string(from: dateOfInfrac as Date)
                let FormattedTime = formatter.string(from: dateOfInfrac as Date)
       
                let coordinate = CLLocationCoordinate2DMake( CLLocationDegrees(lat)!, CLLocationDegrees(long)!)
                let annotation = MyAnnotation(title: "\(userSpeed) MPH in a \(speedLimit) MPH zone", coordinate: coordinate, subtitle: "\(FormattedDate) at \(FormattedTime) - \(timespeed) seconds")

                historyMapView.addAnnotation(annotation)
            }
    
        } catch {
            fatalError("Failed to fetch person: \(error)")
        }
    }

    // MARK: - Navigation

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        
        let dest = segue.destination as? SuperDetailsViewController
        dest?.headline = senderTitle
        dest?.subHeadLine = subSenderTitle
        dest?.locationInfo = cllocation
    }

}
