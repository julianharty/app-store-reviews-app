from __future__ import print_function
import sys
import os
import csv
import lmdb
import review_pb2 as review
import json
from google.protobuf.internal import encoder

with open(sys.argv[1], 'r') as csvf:
   has_header = csv.Sniffer().sniff(csvf.read(1024))
   csvf.seek(0)
   incsv = csv.reader((line.replace('\0','').replace('\r','') for line in csvf), delimiter=",")
   if has_header:
     headers = next(incsv)
   os.remove(sys.argv[2])
   for row in incsv:
       if (len(row)>=16):
           rev = review.Review()
           rev.package_name = row[0]
           if (row[1]):
              rev.app_version_code = int(row[1], 10)
           if (row[2]):
              rev.app_version_name = row[2]
           rev.reviewer_language = row[3]
           rev.device = row[4]
           rev.review_submit_date_and_time = row[5]
           rev.review_submit_millis_since_epoch = int(row[6], 10)
           rev.review_last_update_date_and_time = row[7]
           rev.review_last_update_millis_since_epoch = int(row[8])
           rev.review_star_rating = int(row[9])
           if (row[10]):
              rev.review_title = row[10]
           if (row[11]):
              rev.review_text = row[11]
           if (row[12]):
              rev.developer_reply_data_and_time = row[12]
           if (row[13]):
               try:
                  rev.developer_reply_millis_since_epoch = int(row[13])
               except ValueError as e:
                  a=1
                  #rev.developer_reply_millis_since_epoch = int(row[13])
           if (row[14]):
              rev.developer_reply_text = row[13]
           if (row[15]):
              rev.review_link = row[13]
           f = open(sys.argv[2], "a")
           serializedMessage = rev.SerializeToString()
           delimiter = encoder._VarintBytes(len(serializedMessage))
           f.write(delimiter + serializedMessage)
           f.close()
