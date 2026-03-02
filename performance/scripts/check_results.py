#!/usr/bin/env python3
"""
JMeter Results Analyser
Parses JTL results and fails the build if thresholds are breached.
Usage: python3 check_results.py results.jtl --max-error-rate 1.0 --max-p95-ms 3000
"""
import csv
import sys
import argparse
import statistics

def parse_jtl(file_path):
    samples = []
    with open(file_path, newline='') as f:
        reader = csv.DictReader(f)
        for row in reader:
            samples.append({
                'elapsed': int(row.get('elapsed', 0)),
                'success': row.get('success', 'true').lower() == 'true',
                'label': row.get('label', ''),
                'responseCode': row.get('responseCode', ''),
            })
    return samples

def analyse(samples, max_error_rate, max_p95_ms):
    total = len(samples)
    if total == 0:
        print("No samples found in JTL file")
        sys.exit(1)

    errors = sum(1 for s in samples if not s['success'])
    error_rate = (errors / total) * 100
    elapsed_times = [s['elapsed'] for s in samples]
    elapsed_sorted = sorted(elapsed_times)

    p50 = statistics.median(elapsed_times)
    p95_idx = int(0.95 * len(elapsed_sorted))
    p95 = elapsed_sorted[p95_idx]
    p99_idx = int(0.99 * len(elapsed_sorted))
    p99 = elapsed_sorted[p99_idx]
    avg = statistics.mean(elapsed_times)
    max_time = max(elapsed_times)
    min_time = min(elapsed_times)

    print("=" * 60)
    print("ShopSphere JMeter Performance Results")
    print("=" * 60)
    print(f"Total Samples:    {total}")
    print(f"Errors:           {errors} ({error_rate:.2f}%)")
    print(f"Min Response:     {min_time}ms")
    print(f"Avg Response:     {avg:.0f}ms")
    print(f"P50 Response:     {p50:.0f}ms")
    print(f"P95 Response:     {p95}ms")
    print(f"P99 Response:     {p99}ms")
    print(f"Max Response:     {max_time}ms")
    print("=" * 60)

    passed = True

    if error_rate > max_error_rate:
        print(f"❌ FAIL: Error rate {error_rate:.2f}% exceeds threshold {max_error_rate}%")
        passed = False
    else:
        print(f"✅ PASS: Error rate {error_rate:.2f}% within threshold {max_error_rate}%")

    if p95 > max_p95_ms:
        print(f"❌ FAIL: P95 {p95}ms exceeds threshold {max_p95_ms}ms")
        passed = False
    else:
        print(f"✅ PASS: P95 {p95}ms within threshold {max_p95_ms}ms")

    return passed

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description='Analyse JMeter JTL results')
    parser.add_argument('jtl_file', help='Path to JTL file')
    parser.add_argument('--max-error-rate', type=float, default=1.0)
    parser.add_argument('--max-p95-ms', type=int, default=3000)
    args = parser.parse_args()

    samples = parse_jtl(args.jtl_file)
    success = analyse(samples, args.max_error_rate, args.max_p95_ms)
    sys.exit(0 if success else 1)
