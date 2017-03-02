import sys
import os
import csv
import review_pb2 as review
from google.protobuf.internal import encoder
from Review import *
import flatbuffers

def csv2fbs(input, output):
    # parse csv files from the Android reviews and append to the protobuf records
    with open(input, 'r') as csvf:
       has_header = csv.Sniffer().sniff(csvf.read(1024))
       csvf.seek(0)
       incsv = csv.reader((line.replace('\0','').replace('\r','') for line in csvf), delimiter=",")
       if has_header:
         headers = next(incsv)
       for row in incsv:
           if (len(row)>=16):
               builder = flatbuffers.Builder(0)
               r0 = builder.CreateString(row[0])
               if (row[2]):
                   r2 = builder.CreateString(row[2])
               r3 = builder.CreateString(row[3])
               r4 = builder.CreateString(row[4])
               r5 = builder.CreateString(row[5])
               r7 = builder.CreateString(row[7])
               if (row[10]):
                    r10 = builder.CreateString(row[10])
               if (row[11]):
                    r11 = builder.CreateString(row[11])
               if (row[12]):
                    r12 = builder.CreateString(row[12])
               if (row[14]):
                    r14 = builder.CreateString(row[14])
               if (row[15]):
                    r15 = builder.CreateString(row[15])
               ReviewStart(builder)
               ReviewAddPackageName(builder, r0)
               if (row[1]):
                  ReviewAddAppVersionCode(builder, int(row[1], 10))
               if (row[2]):
                  ReviewAddAppVersionName(builder, r2)
               ReviewAddReviewerLanguage(builder, r3)
               ReviewAddDevice(builder, r4)
               ReviewAddReviewSubmitDateAndTime(builder, r5)
               ReviewAddReviewSubmitMillisSinceEpoch(builder, int(row[6], 10))
               ReviewAddReviewLastUpdateDateAndTime(builder, r7)
               ReviewAddReviewLastUpdateMillisSinceEpoch(builder, int(row[8], 10))
               ReviewAddReviewStarRating(builder, int(row[9]))
               if (row[10]):
                   ReviewAddReviewTitle(builder, r10)
               if (row[11]):
                   ReviewAddReviewText(builder, r11)
               if (row[12]):
                   ReviewAddDeveloperReplyDataAndTime(builder, r12)
               if (row[13]):
                   try:
                       ReviewAddDeveloperReplyMillisSinceEpoch(builder, int(row[13]))
                   except:
                        a = 1
               if (row[14]):
                   ReviewAddDeveloperReplyText(builder, r14)
               if (row[15]):
                   ReviewAddReviewLink(builder, r15)
               rev = ReviewEnd(builder)
               builder.Finish(rev)
               gen_buf, gen_off = builder.Bytes, builder.Head()
               f = open(output, "a")
               f.write(gen_buf[gen_off:])
               f.close()

def csv2pb(input, output):
    # parse csv files from the Android reviews and append to the protobuf records
    with open(input, 'r') as csvf:
       has_header = csv.Sniffer().sniff(csvf.read(1024))
       csvf.seek(0)
       incsv = csv.reader((line.replace('\0','').replace('\r','') for line in csvf), delimiter=",")
       if has_header:
         headers = next(incsv)
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
                   except:
                        a = 1
               if (row[14]):
                  rev.developer_reply_text = row[14]
               if (row[15]):
                  rev.review_link = row[15]
               f = open(output, "a")
               serializedMessage = rev.SerializeToString()
               delimiter = encoder._VarintBytes(len(serializedMessage))
               f.write(delimiter + serializedMessage)
               f.close()

if __name__ == "__main__":
       if (os.path.exists(sys.argv[2])):
           os.remove(sys.argv[2])
       if (sys.argv[3] == "fbs"):
           csv2fbs(sys.argv[1], sys.argv[2])
       if (sys.argv[3] == "pb"):
           csv2pb(sys.argv[1], sys.argv[2])
