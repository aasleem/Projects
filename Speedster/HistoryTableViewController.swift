//
//  HistoryTableViewController.swift
//  Speedster
//
//  Created by Ahmed Sleem on 11/11/16.
//  Copyright © 2016 Hasnain Bilgrami. All rights reserved.
//

import UIKit

class HistoryTableViewController: UITableViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        fetch()
        // Uncomment the following line to preserve selection between presentations
        // self.clearsSelectionOnViewWillAppear = false

        // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
        // self.navigationItem.rightBarButtonItem = self.editButtonItem()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 0
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return 0
    }
    
    func fetch() {
        let moc = DataController().managedObjectContext
        let speedFetch = NSFetchRequest<NSFetchRequestResult>(entityName: "SpeedInfraction")
        
        do {
            let fetchedInfraction = try moc.fetch(speedFetch) as! [SpeedInfraction]
            
            for bob in fetchedInfraction {
                
                //NOTE: Everything but CurrentSpeed returns nil
                
                let userSpeed = bob.currentSpeed! as String
                let speedLimit = bob.speeLimit! as String
                let dateOfInfrac = bob.date!
                let long = bob.longitude!
                let lat = bob.latitdue! as String
                
                let calendar = NSCalendar.current
                let components = calendar.dateComponents([.year, .month, .day, .hour, .minute], from: dateOfInfrac as Date)
                
                
                let coordinate = CLLocationCoordinate2DMake( CLLocationDegrees(lat )!, CLLocationDegrees(long)!)
                let annotation = MyAnnotation(title: "\(userSpeed) MPH in a \(speedLimit) MPH zone", coordinate: coordinate, subtitle: "\(components.month!)-\(components.day!)-\(components.year!) at \(components.hour!):\(components.minute!)")
                //                annotation.coordinate = coordinate
                //                annotation.title = "\(userSpeed) MPH in a \(speedLimit) MPH zone"
                //                annotation.subtitle = "\(components.month!)-\(components.day!)-\(components.year!) at \(components.hour!):\(components.minute!)"
                historyMapView.addAnnotation(annotation)
                
            }
            
        } catch {
            fatalError("Failed to fetch person: \(error)")
        }
    }

    /*
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "reuseIdentifier", for: indexPath)

        // Configure the cell...

        return cell
    }
    */

    /*
    // Override to support conditional editing of the table view.
    override func tableView(_ tableView: UITableView, canEditRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the specified item to be editable.
        return true
    }
    */

    /*
    // Override to support editing the table view.
    override func tableView(_ tableView: UITableView, commit editingStyle: UITableViewCellEditingStyle, forRowAt indexPath: IndexPath) {
        if editingStyle == .delete {
            // Delete the row from the data source
            tableView.deleteRows(at: [indexPath], with: .fade)
        } else if editingStyle == .insert {
            // Create a new instance of the appropriate class, insert it into the array, and add a new row to the table view
        }    
    }
    */

    /*
    // Override to support rearranging the table view.
    override func tableView(_ tableView: UITableView, moveRowAt fromIndexPath: IndexPath, to: IndexPath) {

    }
    */

    /*
    // Override to support conditional rearranging of the table view.
    override func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        // Return false if you do not want the item to be re-orderable.
        return true
    }
    */

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destinationViewController.
        // Pass the selected object to the new view controller.
    }
    */

}
