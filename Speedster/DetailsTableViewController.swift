//
//  DetailsTableViewController.swift
//  Speedster
//
//  Created by Ahmed Sleem on 11/12/16.
//  Copyright © 2016 Hasnain Bilgrami. All rights reserved.
//

import UIKit
import MapKit
import CoreData

class DetailsTableViewController: UITableViewController {
    var listItems = [NSManagedObject]()
    var senderTitle: String!
    var subSenderTitle: String!
    var cllocation: CLLocation!

    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.rowHeight=UITableViewAutomaticDimension
        self.tableView.estimatedRowHeight=44
        fetch()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return listItems.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "history", for: indexPath)
        //fetch()
        
        // Configure the cell...
        let item = listItems[listItems.count-1 - indexPath.row]
        
        let doubleUserSpeed = item.value(forKey: "currentSpeed") as! String
        let fullSubHead: String = doubleUserSpeed
        let fullSubHeadArr = fullSubHead.components(separatedBy: ".")
        let userSpeed: String = fullSubHeadArr[0]
        
        let speedLimit = item.value(forKey: "speeLimit") as! String
        let dateOfInfrac = item.value(forKey: "date")
        let long = item.value(forKey: "longitude")
        let lat = item.value(forKey: "latitdue")
        let timespeed = Double(round(Double(100*(item.value(forKey: "infractionTime") as! Double)))/100)
        
        let dateFormatterGet = DateFormatter()
        dateFormatterGet.dateFormat = "MMM dd, yyyy"
        let formatter = DateFormatter()
        formatter.dateFormat = "hh:mm a"
        
        let FormattedDate = dateFormatterGet.string(from: dateOfInfrac as! Date)
        let FormattedTime = formatter.string(from: dateOfInfrac as! Date)
    
        cell.textLabel?.text = "\(userSpeed) MPH in a \(speedLimit) MPH zone"
        cell.detailTextLabel?.text = "\(FormattedDate) at \(FormattedTime) - \(timespeed) seconds"

        return cell
    }
    
    func fetch() {
        let moc = DataController().managedObjectContext
        let speedFetch = NSFetchRequest<NSFetchRequestResult>(entityName: "SpeedInfraction")
        
        do {
             listItems = try moc.fetch(speedFetch) as! [SpeedInfraction]
        } catch {
            fatalError("Failed to fetch person: \(error)")
        }
    }
 
    override func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        fetch()
        
        senderTitle = tableView.cellForRow(at: indexPath)?.textLabel?.text
        subSenderTitle = tableView.cellForRow(at: indexPath)?.detailTextLabel?.text
        let long = (listItems[indexPath.row].value(forKey: "longitude") as! NSString).doubleValue
        let lat = (listItems[indexPath.row].value(forKey: "latitdue") as! NSString).doubleValue
        
        cllocation = CLLocation(latitude: lat , longitude: long )
        
        self.performSegue(withIdentifier: "superdetailsCell", sender: self)
  }


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

    
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        let dest = segue.destination as? SuperDetailsViewController
        dest?.headline = senderTitle
        dest?.subHeadLine = subSenderTitle
        dest?.locationInfo = cllocation
    }

}
